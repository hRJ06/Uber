package com.Hindol.Uber.Util;

import com.Hindol.Uber.Entity.Driver;
import com.Hindol.Uber.Entity.RideRequest;

import java.time.LocalDateTime;

public class EmailUtil {
    public static String generateRideRequestEmail(RideRequest rideRequest, Driver driver) {
        String driverName = driver.getUser().getName();
        String riderName = rideRequest.getRider().getUser().getName();
        String pickUpLocation = "LATITUDE - " + rideRequest.getPickUpLocation().getX() + " LONGITUDE - " + rideRequest.getPickUpLocation().getY();
        String dropOffLocation = "LATITUDE - " + rideRequest.getDropOffLocation().getX() + " LONGITUDE - " + rideRequest.getDropOffLocation().getY();
        Double fare = rideRequest.getFare();
        String acceptUrl = "http://localhost:8080/drivers/acceptRide/" + rideRequest.getId();

        return "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>New Ride Request</title>" +
                "    <link href=\"https://fonts.googleapis.com/css2?family=Ubuntu:wght@400;500;700&display=swap\" rel=\"stylesheet\">" +
                "    <style>" +
                "        body {" +
                "            font-family: 'Ubuntu', sans-serif;" +
                "            margin: 0;" +
                "            padding: 0;" +
                "            background-color: #f8f8f8;" +
                "        }" +
                "        .container {" +
                "            width: 100%;" +
                "            max-width: 600px;" +
                "            margin: 0 auto;" +
                "            padding: 20px;" +
                "            background-color: #ffffff;" +
                "        }" +
                "        .header {" +
                "            text-align: center;" +
                "            padding: 20px;" +
                "            background-color: #000000;" +
                "            color: white;" +
                "        }" +
                "        .header img {" +
                "            width: 150px;" +
                "        }" +
                "        .content {" +
                "            padding: 20px;" +
                "        }" +
                "        .content h2 {" +
                "            color: #333333;" +
                "            font-size: 18px;" +
                "        }" +
                "        .content p {" +
                "            color: #555555;" +
                "            font-size: 14px;" +
                "            line-height: 1.6;" +
                "        }" +
                "        .button {" +
                "            display: inline-block;" +
                "            background-color: #007bff;" +
                "            color: white;" +
                "            padding: 10px 20px;" +
                "            text-decoration: none;" +
                "            border-radius: 5px;" +
                "            margin-top: 20px;" +
                "        }" +
                "        .button-container {" +
                "            text-align: center;" +
                "        }" +
                "        .footer {" +
                "            padding: 20px;" +
                "            background-color: #f1f1f1;" +
                "            text-align: center;" +
                "            color: #999999;" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"container\">" +
                "        <div class=\"header\">" +
                "            <img src=\"https://freelogopng.com/images/all_img/1659761297uber-icon.png\" alt=\"Uber Logo\">" +
                "            <h1>New Ride Request</h1>" +
                "        </div>" +
                "        <div class=\"content\">" +
                "            <h2>Hello " + driverName + ",</h2>" +
                "            <p>You have a new ride request from a rider. Here are the details:</p>" +
                "            <ul>" +
                "                <li><strong>Rider Name:</strong> " + riderName + "</li>" +
                "                <li><strong>Pickup Location:</strong> " + pickUpLocation + "</li>" +
                "                <li><strong>Dropoff Location:</strong> " + dropOffLocation + "</li>" +
                "                <li><strong>Estimated Fare:</strong> $" + fare + "</li>"  +
                "                <li><strong>Time:</strong> " + LocalDateTime.now() + "</li>" +
                "            </ul>" +
                "            <p>Please review the ride details and accept the request if you're available. To accept, click the button below:</p>" +
                "            <div class=\"button-container\">" +
                "                <a href=\"" + acceptUrl + "\" class=\"button\">Accept Ride</a>" +
                "            </div>" +
                "        </div>" +
                "        <div class=\"footer\">" +
                "            <p>If you have any questions, feel free to contact our support team at support@uber.com.</p>" +
                "            <p>&copy; 2024 Uber Technologies, Inc. All rights reserved.</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }
    public static String generateRideRequestEmailSubject(String riderName) {
        return "New Ride Requested " + riderName;
    }

}
