package ca.uottawa.team3.rentron;

import java.util.Map;

public class Property {

    protected Map<String, Object> propertyData;

    public Property(String address, String type, String floor, String numRoom, String numBathroom, String numFloor, String area, Boolean laundry, String numParkingSpot, String rent, String utilities) {
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
    public String getArea() {
        return this.propertyData.get("area").toString();
    }
    public Boolean getLaundry() {
        return Boolean.valueOf(this.propertyData.get("laundry").toString());
    }
    public String getNumParkingSpot() {
        return this.propertyData.get("numParkingSpot").toString();
    }
    public String getRent() {
        return this.propertyData.get("rent").toString();
    }
    public String getUtilities() {
        return this.propertyData.get("utilities").toString();
    }

    public boolean isValid() {
        return !(this.getAddress().equals("") || this.getType().equals("") ||( this.getType().equals("apartment") && this.getFloor().equals("") )|| this.getNumRoom().equals("") || this.getNumBathroom().equals("") ||  this.getNumFloor().equals("") ||  this.getArea().equals("") ||  this.getLaundry().equals("") ||  this.getNumParkingSpot().equals("") ||  this.getRent().equals("") ||  this.getUtilities().equals(""));
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