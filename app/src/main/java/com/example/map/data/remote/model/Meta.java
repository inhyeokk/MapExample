package com.example.map.data.remote.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class Meta {
    @SerializedName("total_count") private int totalCount;
    @SerializedName("pageable_count") private int pageableCount;
    @SerializedName("is_end") private boolean isEnd;
    @SerializedName("same_name") @Nullable private RegionInfo sameName;

    public int getTotalCount() {
        return totalCount;
    }

    public int getPageableCount() {
        return pageableCount;
    }

    public boolean isEnd() {
        return isEnd;
    }

    @Nullable
    public RegionInfo getSameName() {
        return sameName;
    }
}
