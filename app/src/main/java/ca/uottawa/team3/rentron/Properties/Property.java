package ca.uottawa.team3.rentron.Properties;

import java.util.HashMap;

public class Property {

    protected HashMap<String, Object> propertyData;

    public Property(String address, String type, String floor, String numRoom, String numBathroom, String numFloor, String area, String laundry, String numParkingSpot, String rent, String utilities, String landlord) {
        propertyData = new HashMap<>();
        propertyData.put("address", address);
        propertyData.put("type", type);
        if ("apartment".equals(propertyData.get("type"))) {
            propertyData.put("floor", floor);
        } else {
            propertyData.put("floor", "N/A");
        }
        propertyData.put("numRoom", numRoom);
        propertyData.put("numBathroom", numBathroom);
        propertyData.put("numFloor", numFloor);
        propertyData.put("area", area);
        propertyData.put("laundry", laundry);
        propertyData.put("numParkingSpot", numParkingSpot);
        propertyData.put("rent", rent);
        propertyData.put("utilities", utilities);
        propertyData.put("landlord", landlord);
        propertyData.put("manager","");
        propertyData.put("client","");
    }

    public Property(String address, String type, String floor, String numRoom, String numBathroom, String numFloor, String area, String laundry, String numParkingSpot, String rent, String utilities, String landlord, String manager) {
        propertyData = new HashMap<>();
        propertyData.put("address", address);
        propertyData.put("type", type);
        if ("apartment".equals(propertyData.get("type"))) {
            propertyData.put("floor", floor);
        } else {
            propertyData.put("floor", "N/A");
        }
        propertyData.put("numRoom", numRoom);
        propertyData.put("numBathroom", numBathroom);
        propertyData.put("numFloor", numFloor);
        propertyData.put("area", area);
        propertyData.put("laundry", laundry);
        propertyData.put("numParkingSpot", numParkingSpot);
        propertyData.put("rent", rent);
        propertyData.put("utilities", utilities);
        propertyData.put("landlord", landlord);
        propertyData.put("manager",manager);
        propertyData.put("client","");
    }

    public Property(String address, String type, String unit, String floor, String numRoom, String numBathroom, String numFloor, String area, String laundry, String numParkingSpot, String rent, boolean heating, boolean hydro, boolean water, String landlord, String manager, String client) {
        propertyData = new HashMap<>();
        propertyData.put("address", address);
        propertyData.put("type", type);
        if ("Apartment".equals(propertyData.get("type"))) {
            propertyData.put("floor", floor);
            propertyData.put("unit", unit);
        } else {
            propertyData.put("floor", "N/A");
            propertyData.put("unit", "N/A");
        }
        propertyData.put("numRoom", numRoom);
        propertyData.put("numBathroom", numBathroom);
        propertyData.put("numFloor", numFloor);
        propertyData.put("area", area);
        propertyData.put("laundry", laundry);
        propertyData.put("numParkingSpot", numParkingSpot);
        propertyData.put("rent", rent);

        propertyData.put("heating", heating);
        propertyData.put("water", water);
        propertyData.put("hydro", hydro);

        propertyData.put("landlord", landlord);
        propertyData.put("manager",manager);
        propertyData.put("client",client);
    }

    public String getAddress() {
        return this.propertyData.get("address").toString();
    }
    public String getType() {
        return this.propertyData.get("type").toString();
    }
    public String getFloor() {
        return this.propertyData.get("floor").toString();
    }
    public String getNumRoom() {
        return this.propertyData.get("numRoom").toString();
    }
    public String getNumBathroom() {
        return this.propertyData.get("numBathroom").toString();
    }
    public String getNumFloor() {
        return this.propertyData.get("numFloor").toString();
    }
    public String getUnit() {
        return this.propertyData.get("unit").toString();
    }
    public String getArea() {
        return this.propertyData.get("area").toString();
    }
    public String getLaundry() { return this.propertyData.get("laundry").toString(); }
    public String getNumParkingSpot() {
        return this.propertyData.get("numParkingSpot").toString();
    }
    public String getRent() {
        return this.propertyData.get("rent").toString();
    }
    public boolean getHeating() {
        return Boolean.parseBoolean(this.propertyData.get("heating").toString());
    }
    public boolean getHydro() {
        return Boolean.parseBoolean(this.propertyData.get("hydro").toString());
    }
    public boolean getWater() {
        return Boolean.parseBoolean(this.propertyData.get("water").toString());
    }
    public String getLandlord() {
        return this.propertyData.get("landlord").toString();
    }
    public String getManager() {
        return this.propertyData.get("manager").toString();
    }
    public String getClient() {
        return this.propertyData.get("client").toString();
    }

    public HashMap<String, Object> getPropertyData() { return this.propertyData; }

    public boolean isValid() {
        return !(this.getAddress().equals("") || this.getType().equals("") ||( this.getType().equals("apartment") && this.getFloor().equals("") )|| this.getNumRoom().equals("") || this.getNumBathroom().equals("") ||  this.getNumFloor().equals("") ||  this.getArea().equals("") ||  this.getLaundry().equals("") ||  this.getNumParkingSpot().equals("") ||  this.getRent().equals("")); // || this.getHeating() || this.getHydro() || this.getWater()
    }
}

/*Address.
Type (Basement, Studio, Apartment, Townhouse, House).
If apartment, which floor is the unit in.
Number of rooms.
Number of bathrooms.
Number of floors. [i.e. how many floors in this house]
Total Area.
Laundry In-unit.
Number of parking spots available.
Total Rent.
Utilities included in rent: [Hydro, Heating, Water]*/