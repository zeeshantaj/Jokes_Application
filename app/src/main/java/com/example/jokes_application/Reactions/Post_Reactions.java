package com.example.jokes_application.Reactions;

import android.graphics.drawable.Drawable;

import com.amrdeveloper.reactbutton.Reaction;
import com.example.jokes_application.R;

public final class Post_Reactions {
    public static Reaction defaultReact;

    static {
        defaultReact = new Reaction(
                ReactConstants.REACT,
                ReactConstants.DEFAULT,
                ReactConstants.GRAY,
                R.drawable.baseline_add_reaction_24
                );
    }
    public static Reaction[] reactions = {
            //new Reaction(ReactConstants.LIKE, ReactConstants.BLUE, R.drawable.grey_lile),

            new Reaction(ReactConstants.FUNNY, ReactConstants.RED_LOVE, R.drawable.happy_emoji),
            new Reaction(ReactConstants.GOOD_ONE, ReactConstants.YELLOW_WOW, R.drawable.good_emoji),
            new Reaction(ReactConstants.NOT_BAD, ReactConstants.YELLOW_WOW, R.drawable.bad_emoji),
            new Reaction(ReactConstants.LAME, ReactConstants.YELLOW_WOW, R.drawable.sad_emoji)
            };


}
