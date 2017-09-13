package com.rteixeira.healthlist.data;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * CSV Fields:
 * OrganisationID	OrganisationCode	OrganisationType	SubType	Sector	OrganisationStatus
 * IsPimsManaged OrganisationName	Address1	Address2	Address3	City	County	Postcode
 * Latitude	Longitude	ParentODSCode	ParentName	Phone	Email	Website	Fax
 */
public class Facility implements Parcelable{

    private static final String SEPARATOR = "\t";

    private String OrganisationID;
    private String OrganisationCode;

    private String OrganisationType;
    private String SubType;
    private String Sector;

    private String OrganisationStatus;
    private String IsPimsManaged;

    private String Address1;
    private String Address2;
    private String Address3;
    private String City;
    private String County;
    private String Postcode;

    private double Latitude;
    private double Longitude;

    private String ParentODSCode;
    private String ParentName;

    private String Phone;
    private String Email;
    private String Website;
    private String Fax;

    public Facility() { }

    public Facility(String cvsLine) {
        if (cvsLine == null || cvsLine.isEmpty() || !cvsLine.contains(SEPARATOR) ){
            new Facility();
        } else {
            String[] values = cvsLine.split(SEPARATOR);

            OrganisationID = values[0];
            OrganisationCode = values[1];
            OrganisationType = values[2];
            SubType = values[3];
            Sector = values[4];
            OrganisationStatus = values[5];
            IsPimsManaged = values[6];
            Address1 = values[7];
            Address2 = values[8];
            Address3 = values[9];
            City = values[10];
            County = values[11];
            Postcode = values[12];
            Latitude = Double.parseDouble(values[13]);
            Longitude = Double.parseDouble(values[14]);
            ParentODSCode = values[15];
            ParentName = values[16];
            Phone = values[17];
            Email = values[18];
            Website = values[19];
            Fax = values[20];
        }
    }

    protected Facility(Parcel in) {
        OrganisationID = in.readString();
        OrganisationCode = in.readString();
        OrganisationType = in.readString();
        SubType = in.readString();
        Sector = in.readString();
        OrganisationStatus = in.readString();
        IsPimsManaged = in.readString();
        Address1 = in.readString();
        Address2 = in.readString();
        Address3 = in.readString();
        City = in.readString();
        County = in.readString();
        Postcode = in.readString();
        Latitude = in.readDouble();
        Longitude = in.readDouble();
        ParentODSCode = in.readString();
        ParentName = in.readString();
        Phone = in.readString();
        Email = in.readString();
        Website = in.readString();
        Fax = in.readString();
    }

    public static final Creator<Facility> CREATOR = new Creator<Facility>() {
        @Override
        public Facility createFromParcel(Parcel in) {
            return new Facility(in);
        }

        @Override
        public Facility[] newArray(int size) {
            return new Facility[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(OrganisationID);
        dest.writeString(OrganisationCode);
        dest.writeString(OrganisationType);
        dest.writeString(SubType);
        dest.writeString(Sector);
        dest.writeString(OrganisationStatus);
        dest.writeString(IsPimsManaged);
        dest.writeString(Address1);
        dest.writeString(Address2);
        dest.writeString(Address3);
        dest.writeString(City);
        dest.writeString(County);
        dest.writeString(Postcode);
        dest.writeDouble(Latitude);
        dest.writeDouble(Longitude);
        dest.writeString(ParentODSCode);
        dest.writeString(ParentName);
        dest.writeString(Phone);
        dest.writeString(Email);
        dest.writeString(Website);
        dest.writeString(Fax);
    }

    public String getOrganisationID() {
        return OrganisationID;
    }

    public void setOrganisationID(String organisationID) {
        OrganisationID = organisationID;
    }

    public String getOrganisationCode() {
        return OrganisationCode;
    }

    public void setOrganisationCode(String organisationCode) {
        OrganisationCode = organisationCode;
    }

    public String getOrganisationType() {
        return OrganisationType;
    }

    public void setOrganisationType(String organisationType) {
        OrganisationType = organisationType;
    }

    public String getSubType() {
        return SubType;
    }

    public void setSubType(String subType) {
        SubType = subType;
    }

    public String getSector() {
        return Sector;
    }

    public void setSector(String sector) {
        Sector = sector;
    }

    public String getOrganisationStatus() {
        return OrganisationStatus;
    }

    public void setOrganisationStatus(String organisationStatus) {
        OrganisationStatus = organisationStatus;
    }

    public String getIsPimsManaged() {
        return IsPimsManaged;
    }

    public void setIsPimsManaged(String isPimsManaged) {
        IsPimsManaged = isPimsManaged;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String address1) {
        Address1 = address1;
    }

    public String getAddress2() {
        return Address2;
    }

    public void setAddress2(String address2) {
        Address2 = address2;
    }

    public String getAddress3() {
        return Address3;
    }

    public void setAddress3(String address3) {
        Address3 = address3;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCounty() {
        return County;
    }

    public void setCounty(String county) {
        County = county;
    }

    public String getPostcode() {
        return Postcode;
    }

    public void setPostcode(String postcode) {
        Postcode = postcode;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getParentODSCode() {
        return ParentODSCode;
    }

    public void setParentODSCode(String parentODSCode) {
        ParentODSCode = parentODSCode;
    }

    public String getParentName() {
        return ParentName;
    }

    public void setParentName(String parentName) {
        ParentName = parentName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String website) {
        Website = website;
    }

    public String getFax() {
        return Fax;
    }

    public void setFax(String fax) {
        Fax = fax;
    }
}
