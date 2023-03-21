import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;

public class LocationUpdater implements Runnable {
    private OkHttpClient okHttpClient;
    private Gson gson;
    private Client client;
    private boolean running;

    public LocationUpdater(OkHttpClient okHttpClient, Gson gson, Client client) {
        this.okHttpClient = okHttpClient;
        this.gson = gson;
        this.client = client;
        this.running = false;
    }

    public void start() {
        this.running = true;
        new Thread(this).start();
    }

    public void stop() {
        this.running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                //update location
                client.setLongitude((double) LocationGenerator.generateLongitude());
                client.setLongitude((double) LocationGenerator.generateLatitude());

                // Create a PUT request to update the client's location
                Request request = new Request.Builder()
                        .url("http://localhost:8080/manet/update_location")
                        .put(RequestBody.create(MediaType.parse("application/json"), gson.toJson(client)))
                        .build();

                // Send the request
                try (Response response = okHttpClient.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        ServerResponse serverResponse = gson.fromJson(responseBody, ServerResponse.class);
//                        if (serverResponse.getResponseCode()==ResponseCode.SUCCESS){
//                            //System.out.println("Location updated successfully.");
//                        }else{
//                            //System.out.println("Failed to update location.");
//                        }

                    } else {
                        //System.out.println("Failed to update location.");
                    }
                }

                // Sleep for 10 seconds
                Thread.sleep(10000);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
