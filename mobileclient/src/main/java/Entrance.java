import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import okhttp3.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Entrance {

    private static String username = "";
    private static String password = "";
    private static Client client = new Client();
    private static LocationUpdater updater;
    private static final Gson gson = new Gson();
    private static OkHttpClient okHttpClient = new OkHttpClient();

    public static void register() {

        //random flot number
        client.setLatitude((double) LocationGenerator.generateLatitude());
        client.setLongitude((double) LocationGenerator.generateLongitude());

        // transfer object to json string
        Gson gson = new GsonBuilder().create();
        // Serialize an object to JSON
        String jsonString = gson.toJson(client);

        // Build the request body
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(jsonString,mediaType);

        // Build the request
        //@RequestBody Client client
        Request request = new Request.Builder()
                .url("http://localhost:8080/register")
                .post(body)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {

                ResponseBody responseBody = response.body();
                ServerResponse serverResponse = gson.fromJson(responseBody.string(), ServerResponse.class);

                if (serverResponse.getResponseCode()==ResponseCode.REGISTER_SUCCESS){
                    Double client_id = (Double)serverResponse.getData();
                    client.setId(client_id.intValue());
                    System.out.println(ResponseCode.REGISTER_SUCCESS.getMessage());
                }else{
                    System.out.println(ResponseCode.REGISTER_ERROR.getMessage());
                }

            } else {
                System.out.println("Request failed with HTTP error code: " + response.code());
                System.out.println("Register failed for user: " + username);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while sending the HTTP request: " + e.getMessage());
        }

    }

    public static boolean login(String username, String password) {

        Map<String, String> loginData = new HashMap<>();
        loginData.put("username", username);
        loginData.put("password", password);

        // Build the request body
        Gson gson = new GsonBuilder().create();

        // Convert the user data map to a JSON string
        String requestBody = gson.toJson(loginData);

        // Create a RequestBody object with the JSON string
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), requestBody);

        // Build the request
        //@RequestBody Map<String, String> userInfo
        Request request = new Request.Builder()
                .url("http://localhost:8080/login")
                .post(body)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {

            if (response.isSuccessful()) {

                String responseBody = response.body().string();
                ServerResponse serverResponse = gson.fromJson(responseBody, ServerResponse.class);

                // Check if the Manet was created successfully
                if (serverResponse.getResponseCode()==ResponseCode.LOGIN_SUCCESS) {
                    LinkedTreeMap data = (LinkedTreeMap) serverResponse.getData();
                    client.setId(((Double) data.get("id")).intValue());
                    client.setLongitude((Double) data.get("longitude"));
                    client.setLatitude((Double) data.get("latitude"));
                    client.setNetid(((Double) data.get("netid")).intValue());
                    System.out.println("Login successful for user: " + username);
                    updater = new LocationUpdater(okHttpClient, gson, client);
                    updater.start();
                    return true;
                } else {
                    System.out.println("Login failed for user: " + username + " - " + responseBody);
                    return false;
                }
            } else {
                System.out.println("Request failed with HTTP error code: " + response.code());
                System.out.println("Login failed for user: " + username );
                return false;
            }
        } catch (IOException e) {
            System.out.println("An error occurred while sending the HTTP request: " + e.getMessage());
            return false;
        }
    }

    public static boolean createManet() {
        Gson gson = new Gson();
        //check client is null or not
        if (client == null || client.getId() == null || client.getNetid()!=-1){
            System.out.println(ResponseCode.CREATE_MANET_ERROR);
            return false;
        }



        // Build the request with the request body
        //@RequestParam("clientid") Integer clientid
        //@PostMapping("/manet/create")
        Request request = new Request.Builder()
                .url("http://localhost:8080/manet/create?clientid="+client.getId())
                .post(RequestBody.create(MediaType.parse("application/json"), ""))
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                ServerResponse serverResponse = gson.fromJson(responseBody, ServerResponse.class);

                // Check if the Manet was created successfully
                if (serverResponse.getResponseCode()==ResponseCode.CREATE_MANET_SUCCESS) {
                    Double net_id = (Double) serverResponse.getData();
                    client.setNetid(net_id.intValue());
                    System.out.println("Manet created successfully for user: " + username);
                    return true;

                } else {
                    System.out.println("Manet creation failed for user: " + username + " - " + responseBody);
                    return false;
                }
            } else {
                System.out.println("Request failed with HTTP error code: " + response.code());
                return false;
            }
        } catch (IOException e) {
            System.out.println("An error occurred while sending the HTTP request: " + e.getMessage());
            return false;
        }
    }

    public static boolean logout() {

        //check client is null or not
        if (client == null || client.getId() == null || client.getNetid()!=-1){
            if (client.getNetid()!=-1)
            System.out.println("Please leave the manet first");
            System.out.println(ResponseCode.LOGOUT_ERROR);
            return false;
        }
        Gson gson = new Gson();
        // Build the URL with the username as a query parameter
        // logout(@RequestParam("client_id") Integer client_id)
        String url = "http://localhost:8080/logout?client_id="+client.getId();

        // Build the request
        //    @PutMapping("/logout")
        Request request = new Request.Builder()
                .url(url)
                .put(RequestBody.create(MediaType.parse("application/json"), ""))
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                ServerResponse serverResponse = gson.fromJson(responseBody, ServerResponse.class);

                // Check if the logout was successful
                if (serverResponse.getResponseCode()==ResponseCode.LOGOUT_SUCCESS) {
                    System.out.println(ResponseCode.LOGOUT_SUCCESS.getMessage());
                    client.setNetid(-1);
                    client.setId(null);
                    // When the user logs out
                    updater.stop();
                    return true;
                } else {
                    System.out.println(ResponseCode.LOGOUT_ERROR.getMessage());
                    return false;
                }
            } else {
                System.out.println("Request failed with HTTP error code: " + response.code());
                return false;
            }
        } catch (IOException e) {
            System.out.println("An error occurred while sending the HTTP request: " + e.getMessage());
            return false;
        }
    }

    public static void join()  {

        Integer net_id = -1;

        //check client is null or not
        if (client == null || client.getId() == null || client.getNetid()!=-1){
            System.out.println(ResponseCode.JOIN_MANET_ERROR);
            return;
        }

        //get all available network id that could join----fact: search manet nearby.
        Request request0 = new Request.Builder()
                .url("http://localhost:8080/manet/available")
                .build();

        try (Response response = okHttpClient.newCall(request0).execute()) {

            if (response.isSuccessful()) {

                String responseBody = response.body().string();
                // Process the response body as required
                ServerResponse serverResponse = gson.fromJson(responseBody, ServerResponse.class);

                if (serverResponse.getResponseCode()==ResponseCode.SUCCESS ){

                    List<Manet> data = (List<Manet>) serverResponse.getData();

                    if (data.isEmpty()){
                        System.out.println("currenctly no network nearby");
                        return;
                    }

                    System.out.println("The following manet networks are available:");
                    for (Object obj : data) {
                        Manet manet = gson.fromJson(gson.toJsonTree(obj), Manet.class);
                        System.out.println("Manet ID: " + manet.getNetid());
                        System.out.println("Status: " + (manet.getNetstatus() ? "Active" : "Inactive"));
                        System.out.println("Start Time: " + manet.getStartTime());
                        System.out.println("CurrentConnectAmount: " + manet.getConnectAmount());
                        System.out.println("Capacity: " + manet.getCapacity());
                    }


                    // let user choose net_id
                    // prompt user to choose net_id
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Please enter the net_id of the Manet you want to join:");
                    net_id = scanner.nextInt();

                }else{

                    System.out.println("currenctly no network nearby");

                }

            } else {
                // Handle unsuccessful response
            }
        } catch (IOException e) {
            // Handle IO exception
        }


        //choose one net_id to join ---- fact: join the manet in closest distance.
        if (net_id==-1){
            System.out.println("You need to create a new manet by yourself");
            return;
        }

        Request request = new Request.Builder()
                .url("http://localhost:8080/manet/join?client_id="+client.getId()+"&net_id="+net_id)
                .put(RequestBody.create(MediaType.parse("application/json"), ""))
                .build();


        try {
            // Send the request and wait for the response
            Response response = okHttpClient.newCall(request).execute();
            String responseBody = response.body().string();
            ServerResponse serverResponse = gson.fromJson(responseBody, ServerResponse.class);


            // Handle the response
            if (response.isSuccessful()) {
                if (serverResponse.getResponseCode()==ResponseCode.JOIN_MANET_SUCCESS){
                    Integer netid = ((Double) serverResponse.getData()).intValue();
                    System.out.println(ResponseCode.JOIN_MANET_SUCCESS.getMessage());
                    if (net_id!=netid){
                        System.out.println("Reach to Manet Capacity, Need to Split");
                    }
                    client.setNetid(netid);

                }else{
                    System.out.println(ResponseCode.JOIN_MANET_ERROR.getMessage() );
                }

            } else {
                System.out.println("Failed to join manet: "  );
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    public static void leave()   {
        //check client is null or not
        if (client == null || client.getId() == null || client.getNetid()==-1){
            System.out.println(ResponseCode.LEAVE_MANET_ERROR);
            return;
        }
        Gson gson = new Gson();

        // Create a PUT request to join the manet
        Request request = new Request.Builder()
                .url("http://localhost:8080/manet/leave?client_id="+client.getId())
                .put(RequestBody.create(MediaType.parse("application/json"), ""))
                .build();

        // Send the request and wait for the response
        Response response = null;
        try {

            response = okHttpClient.newCall(request).execute();
            String responseBody = response.body().string();
            ServerResponse serverResponse = gson.fromJson(responseBody, ServerResponse.class);

            // Handle the response
            if (response.isSuccessful()) {
                if (serverResponse.getResponseCode() == ResponseCode.LEAVE_MANET_SUCCESS){
                    System.out.println(ResponseCode.LEAVE_MANET_SUCCESS.getMessage());
                    client.setNetid(-1);
                }else {
                    System.out.println(ResponseCode.LEAVE_MANET_ERROR.getMessage());
                }
            } else {
                System.out.println("Failed to leave manet " );
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    public static void printInfo() {
        System.out.println("Hello " + client.getUsername() + "!");
        System.out.println("Here is your client information:");
        // if id == null print you need login or register first else print id
        if (client.getId() == null) {
            System.out.println("You need to login or register first.");
        } else {
            System.out.println("Your client ID is: " + client.getId());
            // netid
            // if netid == -1 print you didn't join any network
            // else print netid
            if (client.getNetid() == -1) {
                System.out.println("You didn't join any network.");
                System.out.println("You may need to create a network first");
            } else {
                System.out.println("Your network ID is: " + client.getNetid());
            }
            //print your current location:
            System.out.println("Your current location is: (" + client.getLatitude() + ", " + client.getLongitude() + ")");
        }
    }

    public static void main(String[] args) {

        //initialize client
        client.setNetid(-1);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the mobile game!");
        // Get username and password from user input
        System.out.println("Please enter your username to register or login:");
        if (scanner.hasNextLine()) {
            username = scanner.nextLine();
            client.setUsername(username);
            System.out.println("username: " + username);
        }
        System.out.println("Please enter your password to register or login:");
        if (scanner.hasNextLine()) {
            password = scanner.nextLine();
            client.setPassword(password);
            System.out.println("password: " + password);
        }

        printInfo();



        boolean exit = false;
        while (!exit) {
            // Show menu options
            System.out.println("Please choose from the following options:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Create MANET");
            System.out.println("4. Join MANET");
            System.out.println("5. Leave MANET");
            System.out.println("6. Logout");
            System.out.println("7. Exit");
            System.out.println("8. Check Device");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    // Register user with cloud service
                    register();
                    printInfo();
                    break;
                case 2:
                    // Login user with cloud service
                    login(username, password);
                    printInfo();
                    break;
                case 3:
                    // Create MANET
                    createManet();
                    printInfo();
                    break;
                case 4:
                    //  Join MANET
                    join();
                    printInfo();
                    break;
                case 5:
                    // Leave MANET
                    leave();
                    printInfo();
                    break;
                case 6:
                    leave();
                    // Logout user
                    logout();
                    printInfo();
                    break;
                case 7:
                    // exit the program
                    leave();
                    logout();
                    exit = true;
                    System.out.println("bye");
                    break;
                case 8:
                    printInfo();
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }








        scanner.close();
    }
}
