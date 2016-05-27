package dk.vixo.missing_people.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Missing {
    public int id;
    public String name;
    public String description;
    public Date dateOfMissing;
    public Bitmap photoOfMissingPerson;
    public boolean following;
    public boolean volunteering;

    public Missing() {
    }

    public Missing(int id, String name, String description, Date dateOfMissing, Bitmap image, boolean following, boolean volunteering) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dateOfMissing = dateOfMissing;
        this.photoOfMissingPerson = image;
        this.following = following;
        this.volunteering = volunteering;
    }

    public Missing(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getInt("Id");
            this.name = jsonObject.getString("Name");
            this.description = jsonObject.getString("Description");



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateOfMissing() {
        return dateOfMissing;
    }

    public void setDateOfMissing(Date dateOfMissing) {
        this.dateOfMissing = dateOfMissing;
    }

    public Bitmap getPhotoOfMissingPerson() {
        return photoOfMissingPerson;
    }

    public void setPhotoOfMissingPerson(Bitmap photoOfMissingPerson) {
        this.photoOfMissingPerson = photoOfMissingPerson;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public boolean isVolunteering() {
        return volunteering;
    }

    public void setVolunteering(boolean volunteering) {
        this.volunteering = volunteering;
    }
}
