package com.web.chen.messager;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.web.chen.entities.ExcelContent;
import com.web.chen.utils.Exceler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btSend;
    private EditText textMessage;
    private Button btFile;
    private ListView listMessage;
    private static String fileName;

    ArrayList<HashMap<String, String>> item = new ArrayList<>();   //列表中的内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局
        setContentView(R.layout.activity_main);

        //初始化组件
        btSend = (Button) findViewById(R.id.bt_send);
        btFile = (Button) findViewById(R.id.bt_file);
        textMessage = (EditText) findViewById(R.id.text_message);
        listMessage = (ListView) findViewById(R.id.list_message);


        //初始化listView
        final SimpleAdapter simpleAdapter = new SimpleAdapter(this, item, R.layout.listview,
                new String[]{"messageNum", "messageContent"},
                new int[]{R.id.message_num, R.id.message_content});
        listMessage.setAdapter(simpleAdapter);



        //打开文件
        btFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, "选择一个文件"), 2);
            }
        });


        //发送短信
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //要输入的短信内容
                String message = textMessage.getText().toString();
                String[] messagePart = message.split("%s");
                //fileName="/storage/sdcard1/main.xls";//"/storage/sdcard1/main.xlsx";
                //处理excel表格
                if (fileName == null) {
                    Toast.makeText(MainActivity.this, "文件还没选择", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<ExcelContent> excelContentList = Exceler.returnContent(fileName);
                //发短信
                SmsManager manager = SmsManager.getDefault();
                PendingIntent sentIntent = PendingIntent.getBroadcast(MainActivity.this, 0, new Intent(), 0);
                for (ExcelContent content : excelContentList) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    String realMessage = "";
                    int i = 0;
                    for (i = 0; i < messagePart.length - 1; i++) {
                        realMessage += messagePart[i];
                        realMessage += content.getContent().get(i);
                    }
                    realMessage += messagePart[messagePart.length - 1];
                    map.put("messageNum", content.getContent().get(i));
                    map.put("messageContent", realMessage);
                    item.add(map);
                    simpleAdapter.notifyDataSetChanged();
                    manager.sendTextMessage(content.getContent().get(i),null,realMessage,sentIntent,null);
                    //Toast.makeText(MainActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    /**
     * 意图的回调函数
     * @param requestCode 请求码
     * @param resultCode 响应码
     * @param data 数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            Uri contentUri = data.getData();
            fileName = contentUri.getEncodedPath();
            Toast.makeText(MainActivity.this, "已经成功读取" + fileName, Toast.LENGTH_SHORT).show();

        }
    }
}
