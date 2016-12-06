package test.bwie.com.mythirdparty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private UMShareAPI mShareAPI;
    private TextView qq_name;
    private ImageView qq_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button weixin= (Button) findViewById(R.id.weixin);
        Button tv= (Button) findViewById(R.id.shared_text);
        Button qq_text= (Button) findViewById(R.id.qq_text);
        Button xin= (Button) findViewById(R.id.xin);
        qq_name = (TextView) findViewById(R.id.qq_name);
        qq_image = (ImageView) findViewById(R.id.qq_top);

        xin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShareAPI =  UMShareAPI.get( MainActivity.this );
                mShareAPI.getPlatformInfo(MainActivity.this, SHARE_MEDIA.SINA, umAuthListener);
            }
        });

        weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShareAPI =  UMShareAPI.get( MainActivity.this );
                mShareAPI.getPlatformInfo(MainActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
            }
        });

        qq_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShareAPI =  UMShareAPI.get( MainActivity.this );
                mShareAPI.getPlatformInfo(MainActivity.this, SHARE_MEDIA.QQ, umAuthListener);
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareAction(MainActivity.this).setPlatform(SHARE_MEDIA.QQ)
                        .withText("hello")
                        .setCallback(umShareListener)
                        .share();
            }
        });

    }

    //qq
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText(getApplicationContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();
                            Set<String> set = data.keySet();
                            for (String string : set) {

                                // 设置头像
                                if (string.equals("profile_image_url")) {
                                    ImageLoader.getInstance().displayImage(data.get(string), qq_image);
                                }
                                // 设置昵称
                                if (string.equals("screen_name")) {
                                    qq_name.setText(data.get(string));
                                }
                            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText( getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText( getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };
    //分享
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat","platform"+platform);

            Toast.makeText(MainActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(MainActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if(t!=null){
                Log.d("throw","throw:"+t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(MainActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
