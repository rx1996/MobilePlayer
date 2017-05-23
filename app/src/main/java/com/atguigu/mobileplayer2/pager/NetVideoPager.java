package com.atguigu.mobileplayer2.pager;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.mobileplayer2.R;
import com.atguigu.mobileplayer2.activity.SystemVideoPlayerActivity;
import com.atguigu.mobileplayer2.adapter.NetVideoAdapter;
import com.atguigu.mobileplayer2.domain.MediaItem;
import com.atguigu.mobileplayer2.domain.MoveInfo;
import com.atguigu.mobileplayer2.fragment.BaseFragment;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


public class NetVideoPager extends BaseFragment {
    private TextView textView;
    private ListView lv;
    private TextView tv_nodata;
    private NetVideoAdapter adapter;
    private ArrayList<MediaItem> mediaItems;


    //重写视图
    @Override
    public View initView() {
        Log.e("TAG","NetVideoPager-initView");
        View view = View.inflate(context, R.layout.fragment_net_video_pager,null);
        lv = (ListView) view.findViewById(R.id.lv);
        tv_nodata = (TextView) view.findViewById(R.id.tv_nodata);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MoveInfo.TrailersBean item = adapter.getItem(position);
//
//                Intent intent = new Intent(context, SystemVideoPlayerActivity.class);
//                intent.setDataAndType(Uri.parse(item.getUrl()),"video/*");
//                startActivity(intent);

                Intent intent = new Intent(context,SystemVideoPlayerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("videolist",mediaItems);
                intent.putExtra("position",position);
                intent.putExtras(bundle);
                startActivity(intent);



            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        Log.e("TAG","NetVideoPager-initData");
        getDataFromNet();
    }

    private void getDataFromNet() {

        //配置联网请求地址
        final RequestParams request = new RequestParams("http://api.m.mtime.cn/PageSubArea/TrailerList.api");
        x.http().get(request, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                Log.e("TAG","xUtils联网成功=="+result);
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG","xUtils联网失败=="+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 解析json数据和显示数据
     * @param json
     */
    private void processData(String json) {
        MoveInfo moveInfo = new Gson().fromJson(json, MoveInfo.class);
        List<MoveInfo.TrailersBean> datas = moveInfo.getTrailers();
        if(datas != null && datas.size() >0){
            tv_nodata.setVisibility(View.GONE);
            //有数据-适配器
            adapter = new NetVideoAdapter(context,datas);
            lv.setAdapter(adapter);

            mediaItems = new ArrayList<>();
            for(int i = 0; i <datas.size() ; i++) {
                MediaItem mediaItem = new MediaItem();
                mediaItem.setData(datas.get(i).getUrl());
                mediaItem.setName(datas.get(i).getMovieName());
                mediaItems.add(mediaItem);

            }

        }else{
            tv_nodata.setVisibility(View.VISIBLE);
        }





    }
}
