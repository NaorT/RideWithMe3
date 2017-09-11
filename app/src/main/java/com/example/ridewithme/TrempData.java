package com.example.ridewithme;

import android.net.Uri;

public class TrempData {

    public String _name;
    public String _phone;
    public String _from;
    public String _to;
    public String _date;
    public String _time;
    public String _extras;
    public String _timestamp;
    public String _uid;
    public String _key;
    public String _image;


    public TrempData( String _image, String _key, String _uid , String _name , String _phone, String _from, String _to, String _date, String _time, String _extras, String _timestamp) {
        this._date = _date;
        this._to = _to;
        this._time = _time;
        this._phone = _phone;
        this._name = _name;
        this._from = _from;
        this._extras = _extras;
        this._timestamp = _timestamp;
        this._uid = _uid;
        this._key = _key;
        this._image = _image;
    }

    public TrempData( String _key, String _uid , String _name , String _phone, String _from, String _to, String _date, String _time, String _extras, String _timestamp) {
        this._date = _date;
        this._to = _to;
        this._time = _time;
        this._phone = _phone;
        this._name = _name;
        this._from = _from;
        this._extras = _extras;
        this._timestamp = _timestamp;
        this._uid = _uid;
        this._key = _key;

    }

    public TrempData(){}

    public String get_uid() { return _uid; }

    public String get_to() {
        return _to;
    }

    public String get_time() {
        return _time;
    }

    public String get_phone() {
        return _phone;
    }

    public String get_timestamp() {
        return _timestamp;
    }

    public void set_timestamp(String _timestamp) {
        this._timestamp = _timestamp;
    }

    public String get_name() {
        return _name;
    }

    public String get_from() {
        return _from;
    }

    public String get_extras() {
        return _extras;
    }

    public String get_date() { return _date; }

    public void set_name(String _name) {
        this._name = _name;
    }

    public void set_to(String _to) {
        this._to = _to;
    }

    public void set_time(String _time) {
        this._time = _time;
    }

    public void set_phone(String _phone) {
        this._phone = _phone;
    }

    public void set_from(String _from) {
        this._from = _from;
    }

    public void set_extras(String _extras) {
        this._extras = _extras;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public void set_uid(String _uid) { this._uid = _uid; }

    public String get_key() {return _key; }

    public void set_key(String _key) {this._key = _key; }

    public String get_image() {
        return _image;
    }

    public void set_image(String _image) {
        this._image = _image;
    }


}
