package ua.notion.data.dto;

import com.google.gson.annotations.SerializedName;

public record ProtonDTO(@SerializedName("version") String version,
        @SerializedName("name") String name, @SerializedName("url") String url) {

            
}
