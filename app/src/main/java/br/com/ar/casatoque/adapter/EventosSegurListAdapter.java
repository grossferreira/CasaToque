
package br.com.ar.casatoque.adapter;

import br.com.ar.casatoque.R;
import java.util.List;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import br.com.ar.casatoque.modelo.EventosSegur;

public class EventosSegurListAdapter extends ArrayAdapter<EventosSegur> {
        private int resource;
        private LayoutInflater inflater;
        private Context context;


    public EventosSegurListAdapter(Context ctx, int resourceId, List<EventosSegur> objects) {
        super( ctx, resourceId, objects );
        resource = resourceId;
        inflater = LayoutInflater.from( ctx );
        context=ctx;
    }

    @Override
    public View getView ( int position, View convertView, ViewGroup parent ) {
        convertView = ( RelativeLayout ) inflater.inflate( resource, null );
        EventosSegur Legend = getItem( position );
        TextView legendName = (TextView) convertView.findViewById(R.id.legendName);
        legendName.setText(Legend.getName());

        TextView legendBorn = (TextView) convertView.findViewById(R.id.legendBorn);
        legendBorn.setText(Legend.getNick());

        ImageView legendImage = (ImageView) convertView.findViewById(R.id.legendImage);
        String uri = "drawable/" + Legend.getImage();
        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        Drawable image = context.getResources().getDrawable(imageResource);
        legendImage.setImageDrawable(image);

        ImageView NationImage = (ImageView) convertView.findViewById(R.id.Nation);
        uri = "drawable/" + Legend.getNation();
        imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        image = context.getResources().getDrawable(imageResource);
        NationImage.setImageDrawable(image);

        return convertView;
    }
}