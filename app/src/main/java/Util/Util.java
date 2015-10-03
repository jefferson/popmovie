package Util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jeffersonalves.playmovie.DetailActivity;
import com.example.jeffersonalves.playmovie.R;

/**
 * Created by JeffersonAlves on 28/09/2015.
 */
public class Util {

    public static  class Tools{

        private static final String LOG_CAT = Tools.class.getSimpleName();

        public static void calculeHeightListView(ListView listView) {
            int totalHeight = 0;

            ListAdapter adapter = listView.getAdapter();
            int lenght = adapter.getCount();

            listView.requestLayout();

            int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);

            for (int i = 0; i < lenght; i++) {
                View listItem = adapter.getView(i, null, listView);

                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight
                    + (listView.getDividerHeight() * (adapter.getCount() - 1)) + 10;
            listView.setLayoutParams(params);
        }

        public static void calculeHeightListView(ListView listView, int padding) {
            int totalHeight = 0;

            ListAdapter adapter = listView.getAdapter();
            int lenght = adapter.getCount();

            for (int i = 0; i < lenght; i++) {
                View listItem = adapter.getView(i, null, listView);
                listItem.measure(0, 0);
                TextView textView = (TextView) listItem.findViewById(R.id.list_item_review_text_view);
                Integer lines = (Integer)(listItem.getMeasuredWidth()/ DetailActivity.display.getWidth());
                totalHeight += listItem.getMeasuredHeight() * (lines);
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight;
            listView.setLayoutParams(params);

        }

    }
}
