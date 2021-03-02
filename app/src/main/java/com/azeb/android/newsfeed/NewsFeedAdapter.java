package com.azeb.android.newsfeed;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class NewsFeedAdapter extends ArrayAdapter<NewsFeed>{

    int getSectionNameColorSport = ContextCompat.getColor(getContext(),R.color.sport);
    /**
     * Construct a new NewsFeedAdapter
     * @param context
     * @param newsFeeds
     */
    public NewsFeedAdapter(Context context, List<NewsFeed>newsFeeds){
        super(context,0, newsFeeds);
    }
    /**
     * Return a list item View that displays information about the Newsfeed
     * at a given position in the list of newsfeeds.
     */
    @NonNull
    @Override
    //Check if there is an exisiting list item(called convertview)that we can reuse
    //otherwise, if convertview is null, then inflate a new list item layout
    public View getView(int position,  View convertView,  ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.newsfeed_list_item,parent,false);
        }
        // find the newsfeed at the given position in the list of newsfeed
        NewsFeed currentNewsFeed =   getItem(position);
        //find the textView with ID SectionName

        TextView sectionNameView = (TextView) listItemView.findViewById(R.id.sectionName);

        //Display the sectionNmae in the current NewsFeed in that text View

        sectionNameView.setText(currentNewsFeed.getmSectionName());

        //find the textView by Id date

        TextView dateView = (TextView) listItemView.findViewById(R.id.date);

        //DIsplay the date in the current NewsFeed in that text View

        dateView.setText(currentNewsFeed.getmDate());

        //Find the text View Id of Title

        TextView titleView = (TextView) listItemView.findViewById(R.id.news_title);

        //Diplay the NewsTitle in the current Newsfeed in that textView

        titleView.setText(currentNewsFeed.getmTitle());

        TextView authorView = (TextView)listItemView.findViewById(R.id.author);
        if (currentNewsFeed.getmAuthor().size() > 0){
            StringBuilder stringBuilder = new StringBuilder();
            boolean first = true;
            for (int i = 0; i < currentNewsFeed.getmAuthor().size(); i++){
                if (first){
                    first = false;
                }else {
                    stringBuilder.append(System.getProperty("line.separator"));
                }stringBuilder.append(currentNewsFeed.getmAuthor().get(i));
            }authorView.setText(stringBuilder.toString());
        }else {
            authorView.setVisibility(View.GONE);
        }

        //Set the proper background color on each news circle
        //Fetch the background from TextView which is GradientDrawable.

        GradientDrawable sectionNameCircle = (GradientDrawable)sectionNameView.getBackground();

        //Get the approperiate background color on the current news section

        int sectionNameColor = getSectionNameColor(currentNewsFeed.getmSectionName());

        //Set the color on the section circle

        sectionNameCircle.setColor(sectionNameColor);
        return listItemView;
    }
    private int getSectionNameColor(String sectionName){
        int sectionNameColorResourceId;
        switch (sectionName){
            case "Sport":
                sectionNameColorResourceId =R.color.sport;
                break;
            case "Film":
                sectionNameColorResourceId = R.color.film;
                break;
            case "UK news":
                sectionNameColorResourceId = R.color.uk_news;
                break;
            case "World news":
                sectionNameColorResourceId = R.color.world_news;
                break;
            case  "art":
                sectionNameColorResourceId = R.color.art;
                break;
            case "Foot ball":
                sectionNameColorResourceId = R.color.football;
              break;
            case "Opinion":
                sectionNameColorResourceId = R.color.opinion;
                break;
            case "Business":
                sectionNameColorResourceId = R.color.business;
             break;
            default:
                sectionNameColorResourceId = R.color.defult;
                break;
        }

        return ContextCompat.getColor(getContext(), sectionNameColorResourceId);
    }
    }

