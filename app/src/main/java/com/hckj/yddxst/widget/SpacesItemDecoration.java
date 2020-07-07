package com.hckj.yddxst.widget;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private HashMap<String, Integer> valueMap;
    public static final String TOP_DECORATION = "top_decoration";
    public static final String BOTTOM_DECORATION = "bottom_decoration";
    public static final String LEFT_DECORATION = "left_decoration";
    public static final String RIGHT_DECORATION = "right_decoration";

    public SpacesItemDecoration(HashMap<String, Integer> valueMap) {
        this.valueMap = valueMap;
    }

    public SpacesItemDecoration(int bottomDecoration) {
        this.valueMap = new HashMap<>();
        this.valueMap.put(BOTTOM_DECORATION, bottomDecoration);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (valueMap == null)
            return;
        if (valueMap.get(TOP_DECORATION) != null)
            outRect.top = valueMap.get(TOP_DECORATION);
        if (valueMap.get(LEFT_DECORATION) != null)
            outRect.left = valueMap.get(LEFT_DECORATION);
        if (valueMap.get(RIGHT_DECORATION) != null)
            outRect.right = valueMap.get(RIGHT_DECORATION);
        if (valueMap.get(BOTTOM_DECORATION) != null)
            outRect.bottom = valueMap.get(BOTTOM_DECORATION);
    }
}
