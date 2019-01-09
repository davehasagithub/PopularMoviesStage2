package com.example.android.movies.ui.detail;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

import com.example.android.movies.R;

/**
 * This view forces the height of the ConstraintLayout to remain 1.5 times the width.
 * <p>
 * This is a workaround for an issue that may be related to: https://issuetracker.google.com/issues/37133513
 * <p>
 * Issue happens when viewing favorites list, clicking a movie, removing it as a favorite and navigating back.
 * After the item animator completes, the ConstraintLayout that hold the poster grows in height. This corrects
 * itself when the cell is scrolled off screen and back. It appears related to the decoration added to the top
 * row; the issue goes away if the decoration is removed.
 */
public class MoviePosterConstraintLayout extends ConstraintLayout {
    public MoviePosterConstraintLayout(Context context) {
        super(context);
    }

    public MoviePosterConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = (int) (MeasureSpec.getSize(widthMeasureSpec) * getResources().getFraction(R.fraction.movie_poster_height_ratio, 1, 1));
        int heightMode = MeasureSpec.EXACTLY;

        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, heightMode));
    }

}
