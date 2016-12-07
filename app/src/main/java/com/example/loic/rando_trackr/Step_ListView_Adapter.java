package com.example.loic.rando_trackr;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by loic on 07/12/2016.
 */

public class Step_ListView_Adapter extends BaseAdapter {

    Context context;

    String [] step_name;

    String[] datainfo;
    private static LayoutInflater inflater=null;
    public Step_ListView_Adapter(MainActivity mainActivity, String[] step_name, String [] datainfo ) {
        context=mainActivity;

        this.step_name=step_name;
        this.datainfo=datainfo;

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return step_name.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView info;
        ImageView flag;
        TextView data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.step_listview_layout, null);

        //Get the specifique item in the step_listview_layout
        holder.info=(TextView) rowView.findViewById(R.id.step_info);
        holder.flag=(ImageView) rowView.findViewById(R.id.step_flag);
        holder.data=(TextView) rowView.findViewById(R.id.step_distant);

        //Set the values in the textboxes

        //IF it's not the last item
        if(position != (getCount()-1))
        {
            holder.info.setText("Prochaine étape "+(position+1)+":\n"+step_name[position]);
            holder.flag.setImageResource(R.drawable.step_flag);
        }else
        {
            //last item we change the image and the label
            holder.info.setText("Dernière étape "+(position+1)+":\n"+step_name[position]);
            holder.flag.setImageResource(R.drawable.step_finish_flag);
        }
        holder.data.setText("Mètres restant: "+datainfo[position]);
        //Set the on click listenner
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "You Clicked "+step_name[position], Toast.LENGTH_SHORT).show();
            }
        });
        return rowView;
    }
}
