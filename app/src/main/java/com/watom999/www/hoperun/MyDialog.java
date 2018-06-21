package com.watom999.www.hoperun;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.watom999.www.hoperun.utils.PrefUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyDialog extends Dialog implements View.OnClickListener, AdapterView.OnItemClickListener {
    private TextView title;
    private static EditText inputId, inputPassword;
    private Button yes, no;
    private MyDialogCommonListener commonListener;
    private MyDialogListViewItemListener listViewItemlistener;
    private ListView listview;
    private int whichDialog = 0;//默认为0
    private Context context;
    SimpleAdapter simpleAdapter;


    public MyDialog(@NonNull Context context, int whichDialog, MyDialogCommonListener commonListener) {
        super(context);
        this.commonListener = commonListener;
        this.whichDialog = whichDialog;
        this.context = context;
    }

    public MyDialog(@NonNull Context context, int whichDialog, MyDialogListViewItemListener listViewItemlistener) {
        super(context);
        this.listViewItemlistener = listViewItemlistener;
        this.whichDialog = whichDialog;
        this.context = context;
    }

    public MyDialog(Context context, int whichDialog) {
        super(context, R.style.MyDialog);
        this.whichDialog = whichDialog;
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (whichDialog) {
            case 0:
                //加载常规布局
                setContentView(R.layout.dialog_00);
                whoseDialog(0);
                break;
            case 1:
                //加载listview布局
                setContentView(R.layout.dialog_01);
                whoseDialog(1);
                break;
            case 2:

                break;
        }
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);

    }

    private void whoseDialog(int i) {
//        String addr="R.layout.dialog_0"+i;
//        setContentView(Integer.parseInt(addr));
        initView(i);
        initDataOrAdapter(i);
        addListener(i);
    }


    /**
     * 初始化界面控件
     */
    private void initView(int which) {
        switch (which) {
            case 0:
                title = (TextView) findViewById(R.id.title);
                inputId = (EditText) findViewById(R.id.input_id);
                inputPassword = (EditText) findViewById(R.id.input_password);
                yes = (Button) findViewById(R.id.yes);
                break;
            case 1:
                title = (TextView) findViewById(R.id.title);
                listview = (ListView) findViewById(R.id.listview_dialog);
                break;
            case 2:

                break;
        }
    }

    private void initDataOrAdapter(int which) {
        switch (which) {
            case 0:

                break;
            case 1:
                listview.addHeaderView(new ViewStub(context));
                simpleAdapter = new SimpleAdapter(context, setData(), R.layout.listview_item01, new String[]{"icon", "name", "id"}, new int[]{R.id.image_view, R.id.user_name, R.id.user_id});
                listview.setAdapter(simpleAdapter);
                break;
            case 2:

                break;
        }

    }

    private List<HashMap<String, Object>> setData() {
        List<HashMap<String, Object>> list = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();

        map.put("icon", "");
        map.put("name", "");
        map.put("id", "");
        list.add(map);

        return list;
    }

    private void addListener(int which) {
        switch (which) {
            case 0:
                yes.setOnClickListener(this);
                no.setOnClickListener(this);
                break;
            case 1:
                listview.setOnItemClickListener(this);
                break;
            case 2:

                break;
        }
    }

    public interface MyDialogCommonListener {
        public void onCommonClick(View view);
    }

    public interface MyDialogListViewItemListener {
        public void onListViewItemClick(AdapterView<?> adapterView, View view, int i, long l);
    }

    @Override
    public void onClick(View view) {
        commonListener.onCommonClick(view);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        listViewItemlistener.onListViewItemClick(adapterView, view, i, l);
    }

    /**
     * 获取dialog中的数据
     *
     * @param context
     * @return
     */
    public static Map<String, String> getInputData(Context context) {
        String id, password;
        HashMap<String, String> map = new HashMap<>();
        if (null == PrefUtils.getString("id", null, context)) {
            id = inputId.getText().toString();
            password = inputPassword.getText().toString();
            PrefUtils.putString("id", id, context);
            PrefUtils.putString("password", password, context);
        } else {
            //从SP文件中拿出数据。并装在Map集合中返回调用处。
            map.put("id", PrefUtils.getString("id", null, context));
            map.put("password", PrefUtils.getString("password", null, context));
        }
        return map;
    }
}