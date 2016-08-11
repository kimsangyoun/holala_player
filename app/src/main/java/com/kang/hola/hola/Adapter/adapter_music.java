package com.kang.hola.hola.Adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kang.hola.hola.R;
import com.kang.hola.hola.Vo.MusicVo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adapter_music extends BaseAdapter{

	private Activity m_activity;
	ArrayList<MusicVo> mv ;
	LayoutInflater mInflater;

	public adapter_music(Activity con, ArrayList<MusicVo> MV)
	{
		m_activity= con;
		this.mv = MV;
		mInflater = (LayoutInflater)m_activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return mv.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


	public View getView(int position, View convertView, ViewGroup parent) {
		final int pos = position;
		ViewHolder holder;

		if(convertView == null){
			convertView = mInflater.inflate(R.layout.listview_music, parent,false);
			holder = new ViewHolder();
			holder.photo=(ImageView)convertView.findViewById(R.id.mlist_img01);
			holder.singer =(TextView) convertView.findViewById(R.id.mlist_singer);
			holder.title = (TextView) convertView.findViewById(R.id.mlist_title);

            convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Uri uri = Uri.parse("content://media/external/audio/albumart/" + mv.get( position).salbumid);
		Picasso.with(m_activity).load(uri).placeholder(R.drawable.music_default).error(R.drawable.music_default).resize(175, 175).into(holder.photo);



		holder.singer.setText(mv.get(position ).sartist);
		holder.title.setText(mv.get( position ).stitle);
	//	convertView.setOnClickListener(new OnClickListener() {
           // public void onClick(View v) {
            //    GoIntent(pos);
			//}
		//});
		return convertView;
	}
	public void GoIntent(int pos){
		//Intent intent = new Intent(m_activity, ReadActivity.class);
	/*	intent.putExtra("sangho", sl.get((getCount() - pos - 1)).SANGHO);
		intent.putExtra("content", sl.get((getCount() - pos - 1)).CONTENT);
		intent.putExtra("picture", sl.get((getCount() - pos - 1)).Picture);
		intent.putExtra("location", sl.get((getCount() - pos - 1)).LOCATION);

	*/
		//intent.put
		//m_activity.startActivity(intent);
	}

private static class ViewHolder{
	public ImageView photo;
	public TextView title;
	public TextView singer;


}


}
