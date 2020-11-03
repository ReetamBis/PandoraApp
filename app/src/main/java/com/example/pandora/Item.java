package com.example.pandora;

import android.net.Uri;

public class Item {
    private String name;
    private Uri uri;

    public Item(String name, Uri uri) {
        this.name = name;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public Uri getUri() {
        return uri;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
