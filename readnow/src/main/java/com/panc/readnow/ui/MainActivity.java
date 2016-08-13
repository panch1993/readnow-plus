package com.panc.readnow.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.panc.readnow.R;
import com.panc.readnow.app.App;
import com.panc.readnow.app.Constant;
import com.panc.readnow.bean.DataBean;
import com.panc.readnow.datamanger.CacheManger;
import com.panc.readnow.datamanger.GsonUtil;
import com.panc.readnow.datamanger.HttpManager;
import com.panc.readnow.util.SPUtils;
import com.panc.readnow.util.ToastUtils;
import com.panc.readnow.util.Utils;
import com.panc.readnow.view.composerLayout;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.tv_content)
    TextView mTvContent;
    @Bind(R.id.tv_author)
    TextView mTvAuthor;
    @Bind(R.id.tv_time)
    TextView mTvTime;
    @Bind(R.id.cb_show_sbt)
    CheckBox mCbShowSbt;
    @Bind(R.id.listview_favorite)
    ListView mListviewFavorite;
    @Bind(R.id.scrollview)
    ScrollView mScrollview;
    @Bind(R.id.btns_super)
    composerLayout mBtnSuper;
    @Bind(R.id.dl_main)
    DrawerLayout mDlMain;
    @Bind(R.id.tv_like)
    TextView mTvLike;
    @Bind(R.id.tv_end)
    TextView mTvEnd;
    @Bind(R.id.ll_left)
    LinearLayout mLlLeft;
    @Bind(R.id.rb_light)
    RadioButton mRbLight;
    @Bind(R.id.rb_night)
    RadioButton mRbNight;
    @Bind(R.id.rg_theme)
    RadioGroup mRgTheme;
    @Bind(R.id.rl_main)
    RelativeLayout mRlMain;
    @Bind(R.id.bt_light)
    Button mBtLight;
    @Bind(R.id.bt_night)
    Button mBtNight;
    @Bind(R.id.iv_tip)
    ImageView mIvTip;
    private DataBean mGsonDataBean;
    private String mJson;
    private MyAdapter mMyAdapter;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    public static final String THEME = "THEME";
    private boolean isLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.bind(this);

        isLight = SPUtils.getBoolean(this, THEME, true);
        if (isLight) {
            setThemeColor(Utils.getColor(R.color.light_bg), Utils.getColor(R.color.light_text));
            mRbLight.setChecked(true);
        } else {
            setThemeColor(Utils.getColor(R.color.night_bg), Utils.getColor(R.color.night_text));
            mRbNight.setChecked(true);
        }

        getDateByDay(getFormatDate());
        initActionBar();
        initBtnSuper();
        initEvent();
        initFilePath();
    }

    /**
     * 更改颜色
     * @param colorBg
     * @param colorText
     */
    private void setThemeColor(int colorBg, int colorText) {
        mScrollview.setBackgroundColor(colorBg);
        mTvAuthor.setTextColor(colorText);
        mTvContent.setTextColor(colorText);
        mTvLike.setTextColor(colorText);
        mTvTitle.setTextColor(colorText);
        mTvEnd.setTextColor(colorText);
        mTvTime.setTextColor(colorText);
        mBtLight.setTextColor(colorText);
        mBtNight.setTextColor(colorText);
        mLlLeft.setBackgroundColor(colorBg);
        if (mMyAdapter != null) {
            mMyAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 监听
     */
    private void initEvent() {
        mRgTheme.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_light) {
                    setThemeColor(Utils.getColor(R.color.light_bg), Utils.getColor(R.color.light_text));
                    SPUtils.saveBoolean(MainActivity.this, THEME, true);
                } else if (checkedId == R.id.rb_night) {
                    setThemeColor(Utils.getColor(R.color.night_bg), Utils.getColor(R.color.night_text));
                    SPUtils.saveBoolean(MainActivity.this, THEME, false);
                }
            }
        });
        mCbShowSbt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mBtnSuper.setVisibility(View.VISIBLE);
                } else {
                    mBtnSuper.setVisibility(View.GONE);
                }
            }
        });
        mIvTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Tip");
                builder.setMessage("文章内容基于原版app\n" +
                        "手势操作改为按钮操作\n添加了本地收藏功能\n收藏文章保存在本地sd卡中的\ncom.panc.readnow/cache目录里\n删除了就没有咯~" +
                        "\n还有什么要改进的可以直接告诉我\n" +
                        "vx：walkwindp");
                builder.setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }

    private void initFilePath() {
        File path = new File(CacheManger.getInstance().cacheDir);
        String[] strings = path.list();
        List<String> titles = new ArrayList<>();
        for (String string : strings) {
            titles.add(string);
        }
        mMyAdapter = new MyAdapter();
        mMyAdapter.setDate(titles);
        mListviewFavorite.setAdapter(mMyAdapter);
        mListviewFavorite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = CacheManger.getInstance().getText((String) mMyAdapter.getItem(position));
                DataBean dataBean = GsonUtil.parseJsonToBean(text, DataBean.class);
                updateView(dataBean);
                mDlMain.closeDrawers();
            }
        });
    }

    private void initBtnSuper() {
        // 引用控件
        mBtnSuper.init(new int[]{R.drawable.icon_befor,
                        R.drawable.icon_random, R.drawable.icon_favor,
                        R.drawable.icon_after}, R.drawable.icon_,
                R.drawable.composer_icn_plus, composerLayout.RIGHTBOTTOM, 260,
                300);
        // 加個點擊監聽，100+0對應composer_camera，100+1對應composer_music……如此類推你有幾多個按鈕就加幾多個。
        View.OnClickListener clickit = new View.OnClickListener() {
            //从左向右
            @Override
            public void onClick(View v) {
                if (v.getId() == 100 + 0) {
                    mScrollview.smoothScrollTo(0, 0);
                    getDateByDay(getOtherDay(mTvTime.getText().toString(), true));
                    ToastUtils.showToast("前一天");
                } else if (v.getId() == 100 + 1) {
                    mScrollview.smoothScrollTo(0, 0);
                    random();
                    ToastUtils.showToast("随机文章");
                } else if (v.getId() == 100 + 2) {
                    if (mJson == null || mGsonDataBean == null) {
                        return;
                    }
                    boolean isSave = CacheManger.getInstance().saveText(mJson, mGsonDataBean.title);
                    if (isSave) {
                        ToastUtils.showToast("收藏成功");
                        mMyAdapter.addDate(mGsonDataBean.title);
                    }
                } else if (v.getId() == 100 + 3) {
                    mScrollview.smoothScrollTo(0, 0);
                    getDateByDay(getOtherDay(mTvTime.getText().toString(), false));
                    ToastUtils.showToast("后一天");
                }
            }
        };
        mBtnSuper.setButtonsOnClickListener(clickit);
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();

        //设置箭头
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        actionBar.setTitle("每日一文+");
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDlMain, R.string.open, R.string.close);

        mActionBarDrawerToggle.syncState();

        mDlMain.addDrawerListener(mActionBarDrawerToggle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mActionBarDrawerToggle.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 随机文章
     */
    private void random() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mJson = HttpManager.getInstance().dataGet(Constant.RANDOM);
                mGsonDataBean = GsonUtil.parseJsonToBean(mJson, DataBean.class);
                if (mGsonDataBean == null) {
                    return;
                }
                updateView(mGsonDataBean);
            }
        }).start();


    }

    /**
     * 根据日期获取
     *
     * @param date
     */
    private void getDateByDay(String date) {
        final String todayUrl = String.format(Constant.DATE, date);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mJson = HttpManager.getInstance().dataGet(todayUrl);
                mGsonDataBean = GsonUtil.parseJsonToBean(mJson, DataBean.class);
                if (mGsonDataBean == null || mGsonDataBean.content == null) {
                    Utils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast("没有数据~");
                        }
                    });
                    return;
                }
                updateView(mGsonDataBean);
            }
        }).start();
    }

    /**
     * 刷新文本
     *
     * @param gsonDataBean
     */
    private void updateView(final DataBean gsonDataBean) {
        final String content = gsonDataBean.content.replace("<p>", "\t  \t");
        Utils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                mTvContent.setText(content.replace("</p>", "\n"));
                mTvTitle.setText(gsonDataBean.title);
                mTvAuthor.setText(gsonDataBean.author);
                mTvTime.setText(gsonDataBean.date);
                //滑到顶部
                mScrollview.smoothScrollTo(0, 0);
            }
        });
    }

    /**
     * 今日日期
     *
     * @return
     */
    public String getFormatDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(new Date());
    }

    /**
     * 前一天|后一天
     *
     * @param currentday
     * @param isBefor
     * @return
     */
    public String getOtherDay(String currentday, boolean isBefor) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        long anotherDay = 0;
        try {
            if (isBefor) {
                anotherDay = sdf.parse(currentday).getTime() - 86400000;
            } else {
                anotherDay = sdf.parse(currentday).getTime() + 86400000;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf.format(anotherDay);
    }

    // 双击退出功能实现
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再次点击返回桌面",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                // System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
/*

    @OnClick({R.id.bt_light, R.id.bt_night})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_light:
                setThemeColor(Utils.getColor(R.color.light_bg), Utils.getColor(R.color.light_text));
                SPUtils.saveBoolean(this, THEME, true);
                break;
            case R.id.bt_night:
                setThemeColor(Utils.getColor(R.color.night_bg), Utils.getColor(R.color.night_text));
                SPUtils.saveBoolean(this, THEME, false);
                break;
        }
    }
*/

    class MyAdapter extends BaseAdapter {
        private List<String> titles = new ArrayList<>();

        public void addDate(String title) {
            titles.add(title);
            notifyDataSetChanged();
        }

        public void setDate(List<String> list) {
            titles = list;
        }

        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public Object getItem(int position) {
            return titles.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(App.context, R.layout.item_favorite_lv, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.mTvTitle.setText(titles.get(position));
            if (isLight) {
                holder.mTvTitle.setTextColor(Utils.getColor(R.color.light_text));
            } else {
                holder.mTvTitle.setTextColor(Utils.getColor(R.color.night_text));
            }
            holder.mIvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isDelete = CacheManger.getInstance().deleteFile(titles.get(position));
                    if (isDelete) {
                        titles.remove(position);
                        notifyDataSetChanged();
                        ToastUtils.showToast("删除成功");
                    }
                }
            });
            return convertView;
        }

        class ViewHolder {
            @Bind(R.id.tv_title)
            TextView mTvTitle;
            @Bind(R.id.iv_delete)
            ImageView mIvDelete;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
