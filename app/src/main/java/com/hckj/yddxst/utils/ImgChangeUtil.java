package com.hckj.yddxst.utils;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * ClassName:ImgChangeUtil
 * Package:com.hckj.yddx.utils
 * Description:
 *
 * @date:2020/6/30 16:15
 * @author:1477083563@qq.com
 */
public class ImgChangeUtil {

    public void imgChange(RecyclerView recyclerView, ImageView btnPrevious, ImageView btnNext,int crollV){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    //获取最后一个可见view的位置
                    int lastItemPosition = linearManager.findLastVisibleItemPosition();
                    int localPosition = ((RecyclerView.LayoutParams) recyclerView.getChildAt(0).getLayoutParams()).getViewAdapterPosition();
                    int totalItemCount = linearManager.getItemCount();
                    if(crollV == 300){
                        if(totalItemCount<=5){
                            btnPrevious.setVisibility(View.INVISIBLE);
                            btnNext.setVisibility(View.INVISIBLE);
                        }else {
                            if(firstItemPosition!=0&&lastItemPosition<(totalItemCount-1)){
                                btnNext.setVisibility(View.VISIBLE);
                                btnPrevious.setVisibility(View.VISIBLE);
                            }else if(lastItemPosition==(totalItemCount-1)){
                                btnNext.setVisibility(View.INVISIBLE);
                                btnPrevious.setVisibility(View.VISIBLE);
                            }else if(firstItemPosition==0){
                                btnPrevious.setVisibility(View.INVISIBLE);
                                btnNext.setVisibility(View.VISIBLE);
                            }
                        }
                    }else if(crollV == 1006){
                        if(totalItemCount<=5){
                            btnPrevious.setVisibility(View.INVISIBLE);
                            btnNext.setVisibility(View.INVISIBLE);
                        }else if(totalItemCount==6){

                        } else if (totalItemCount<=10&&totalItemCount>5){
                            if(firstItemPosition==0){
                                btnPrevious.setVisibility(View.INVISIBLE);
                                btnNext.setVisibility(View.VISIBLE);
                            }else if(lastItemPosition==(totalItemCount-1)){
                                btnNext.setVisibility(View.INVISIBLE);
                                btnPrevious.setVisibility(View.VISIBLE);
                            }
                        }else if (totalItemCount>10){
                            if(firstItemPosition!=0&&lastItemPosition<(totalItemCount-1)){
                                btnNext.setVisibility(View.VISIBLE);
                                btnPrevious.setVisibility(View.VISIBLE);
                            }else if(lastItemPosition==(totalItemCount-1)){
                                btnNext.setVisibility(View.INVISIBLE);
                                btnPrevious.setVisibility(View.VISIBLE);
                            }else if(firstItemPosition==0){
                                btnPrevious.setVisibility(View.INVISIBLE);
                                btnNext.setVisibility(View.VISIBLE);
                            }
                        }

                    }

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
}
