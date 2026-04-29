package com.ynwlzc.framework.cyberwinosand_ScreenDisplay;

import android.Manifest;
import android.annotation.SuppressLint;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;

import CyberWinPHP.CyberWin_MicroAPP.CyberWin_MicroAPP_Android;
import CyberWinPHP.Cyber_CPU.Cyber_Public_Var;
import CyberWinPHP.Cyber_CPU.iniFile;
import CyberWinPHP.Cyber_DataBase.Cyber_M;
import CyberWinPHP.Cyber_Plus.Cyber_JsPrinterStandard;
import CyberWinPHP.Cyber_Plus.Cyber_JsVOS_Studio;
import CyberWinPHP.Cyber_Plus.LogToFile;
import CyberWinPHP.Cyber_Server.Cyber_Server_Web;
import CyberWinPHP.SmartScreen.CyberWin_Smart_CommonFunction;
import CyberWinPHP.SuperHardSysten.CyberWin_Tickets_Device;
//import CyberWinPHP.Cyber_Plus;
import static CyberWinPHP.Cyber_CPU.Cyber_Public_Var.Application_StartupPath;
/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
   // private View mContentView;

    public WebView cwpd_Web;

    final Context cyber_instance = this;

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            /*
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            */
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);

        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (AUTO_HIDE) {
                        delayedHide(AUTO_HIDE_DELAY_MILLIS);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        Cyber_Public_Var.m_cpu = this;

       // MyApplication.m_cpu = this;
        Cyber_Public_Var.cyber_main_instance = this;

        LogToFile.init(this);

        mVisible = true;
      //  mControlsView = findViewById(R.id.fullscreen_content_controls);
      //  mContentView = findViewById(R.id.fullscreen_content);

        // Set up the user interaction to manually show or hide the system UI.
        /*
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
        */
        String DefaultHome="";
        try{

            verifyStoragePermissions();

            //2024-12-03
            String 根目录 = CyberWin_Smart_CommonFunction.获取程序运行目录();//东方仙盟_系统_获取根目录();
            Cyber_Public_Var.CyberWin_智能屏幕路径=Cyber_Public_Var.CyberWin_智能屏幕路径.replace("%CyberWinPHP_Path%", 根目录)
                    .replace("%CyberWinPHP_root%",根目录);
            CyberWin_Smart_CommonFunction.东方仙盟_智慧大屏幕_检测资源目录();
            //系列化
            iniFile IR = new iniFile();
            Cyber_Public_Var.cwpd_device_sn = Settings.System.getString(this.getContentResolver(), Settings.System.ANDROID_ID);
              DefaultHome = IR.GetINI("APP", "DefaultHome", "", "CyberWin").replace("%CyberWinPHP_Path%", Application_StartupPath);// Application.StartupPath + "/CyberWinPHP");;

            Cyber_Public_Var.Application_StartupPath_Home = DefaultHome;
            Cyber_Public_Var.client_sn=Cyber_JsPrinterStandard.getDeviceUUid();

            //CyberWin_MicroApp_local_path
            //2024-12-26
            String 根目录磁盘 = Environment.getExternalStorageDirectory().getAbsolutePath();

          // Cyber_Public_Var.CyberWin_MicroApp_local_path =Cyber_Public_Var.CyberWin_MicroApp_local_path
         //          .replace("%CyberWinPHP_root%",根目录磁盘);


            Cyber_Public_Var.CyberWin_MicroApp_local_path =Cyber_Public_Var.CyberWin_MicroApp_local_path
                    .replace("%CyberWinPHP_root%",根目录);
            //App_local_path
            Cyber_Public_Var.App_local_path =Cyber_Public_Var.App_local_path
                    .replace("%CyberWinPHP_root%",根目录);

            _cyber_initClientParamFromConfig();

            //判断飘动
            /*
            String folat_pay = IR.GetINI("APP", "show_window_payonly", "", "CyberWin").replace("%CyberWinPHP_Path%", Application_StartupPath);// Application.StartupPath + "/CyberWinPHP");;
            if (folat_pay.equals("1")) {
                cwpd_createFloatView_scanpay();
            }
            */

            //数据写入
            Cyber_M cyber_m = new Cyber_M("system_config", "cyberwin_");
            //  cyber_m.where(where);
            //  String  DefaultHome2=cyber_m.GetFieldV2("value");
            // LogToFile.d("未来之窗配置","系统第二梦="+DefaultHome2);
            HashMap<String, String> data_add = new HashMap<String, String>();
            data_add.put("config_class", "CyberWin");
            data_add.put("section", "support");
            data_add.put("key", "tel");
            data_add.put("value", "18101051931");
            //cyber_m.Add(data_add);

        }catch (Exception ex) {
        LogToFile.d("Exception", "启动异常" + ex.getMessage() + ex.getStackTrace());
    }

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
      //  findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
        initView();
        initWebView_Plugs();
        initWebView();

        未来之窗_人工智能_客户首页();

        //2024-12-03
        //常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //启动服务
        StartLocalServer();

        /*
        String HOME_布草厂="http://51.onelink.ynwlzc.net/o2o/wap.php?g=Wap&c=Card_new_membersManager&a=index_Hotel_Linen_byarea";
        String HOME_报销 = "http://51.onelink.ynwlzc.net/o2o/wap.php?g=Wap&c=Merchant_Manager_Report_BookkeepingUser&a=store_inout_apply";
        String HOME_酒店押金 = "http://51.onelink.ynwlzc.net/o2o/wap.php?g=Wap&c=Merchant_Manager_hotel&a=hotel_deposit_light_v2024";


        String HOME_万能押金单20240709 = "http://yj.ynwlzc.cn/cyberwin_w.php?g=Wap&c=Merchant_Manager_CommonDeposit&a=deposit_order_manager_Simplified";


        //  cwpd_Web.loadUrl(HOME_布草厂);
       // cwpd_Web.loadUrl(HOME_报销);
        HOME_酒店押金 ="http://yj.ynwlzc.cn/cyberwin_w.php?g=Wap&c=Merchant_Manager_hotel&a=hotel_deposit_light_v2024";
//http://51.onelink.ynwlzc.net/o2o/index.php/startlink/1
        String HOME_pos工作台20240802 = "http://51.onelink.ynwlzc.net/o2o/index.php/startlink/1";

        String HOME_pos广发美发=  "http://shop.gfrj.top/mf_app/cohpp.html";

      // cwpd_Web.loadUrl(HOME_酒店押金);
       // cwpd_Web.loadUrl(HOME_万能押金单20240709);
        cwpd_Web.loadUrl(HOME_pos广发美发);
      //  cwpd_Web.loadUrl(HOME_报销);
        */

        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {

            Uri data = intent.getData();
            if (data != null) {
                // 根据数据的类型和内容进行处理
                String filePath = data.getPath();
                String 文件专业转化 =   Cyber_Public_Var.getFileAbsolutePath(Cyber_Public_Var.cyber_main_instance,data);
                // 可以对文件路径进行进一步处理

                Toast.makeText(Cyber_Public_Var.cyber_main_instance, "关联启动"+filePath+",文件专业转化="+文件专业转化, Toast.LENGTH_SHORT).show();

                if ("application/pdf".equals(intent.getType())) {
                    // 处理 PDF 文件相关逻辑，比如使用 PDF 查看库打开文件
                } else {
                    // 处理其他类型的数据
                    String HOME_未来之窗手VOSIDE编辑器= "file:///android_asset/app/CyberTrade_ecogen_VOS_Studio/cus_app/CyberWin_TradeOpenEditor/androidfileeditor_main.html";
                    HOME_未来之窗手VOSIDE编辑器 = HOME_未来之窗手VOSIDE编辑器+"?url="+ 文件专业转化;

                    cwpd_Web.loadUrl(HOME_未来之窗手VOSIDE编辑器);
                    //  cwpd_Web.loadUrl(HOME_报销);

                }
            }
        }
    }

    private void _cyber_initClientParamFromConfig() {

        Cyber_JsPrinterStandard csp = new Cyber_JsPrinterStandard(this,this);

        Cyber_Public_Var.Application_POs_dev_sn = csp.cwpd_system_config_merchant_get("Pay", "device_sn", "", "20225578-2019");
//Application_Safe_wlzc_authen_pass
        Cyber_Public_Var.Application_Safe_wlzc_authen_pass = csp.cwpd_system_config_merchant_get("Safe", "wlzc_authen_pass", "", "20225578-2019");
//Ap
        //密码控制
        if (Cyber_Public_Var.Application_Safe_wlzc_authen_pass.length() > 8) {
            Cyber_Public_Var.Application_StartupPath_Home = Cyber_Public_Var.Application_StartupPath_Home + "&wlzc_authen_pass="
                    + Cyber_Public_Var.Application_Safe_wlzc_authen_pass;
        }
        //cwpd_debug_mod
        String debug = csp.cwpd_system_get("APP", "debug", "20225578-2019");
        if (debug.equals("1")) {
            Cyber_Public_Var.cwpd_debug_mod = true;
        }else{
            Cyber_Public_Var.cwpd_debug_mod = false;
        }

        //读取默认打印机
        Cyber_Public_Var.cyberwin_default_printer = csp.cwpd_system_get("printer", "default", "20225578-2019");
        //读取默认网络打印
        //读取默认打印类型
        Cyber_Public_Var.cyberwin_default_printer_net = csp.cwpd_system_get("printer", "net", "20225578-2019");
        Cyber_Public_Var.cyber_default_printer_type = csp.cwpd_system_get("printer", "devicetype", "20225578-2019");
        Cyber_Public_Var.cyber_default_printer_report_width = csp.cwpd_system_get("printer", "report_width", "20225578-2019");
        //未来之窗启动模式
        Cyber_Public_Var.App_start_mode = csp.cwpd_system_get("APP", "start_mode", "20225578-2019");
        //未来之窗启动模式

        LogToFile.d_windows("卷宗","模式", Cyber_Public_Var.App_start_mode);

        //读取蓝牙
        // Cyber_Public_Var.uuid = csp.cwpd_system_get("printer", "net", "20225578-2019");
        Cyber_Public_Var.App_localcache_kernel = csp.cwpd_system_get("LocalCache", "kernel", "20225578-2019");
        Cyber_Public_Var.App_localcache_image = csp.cwpd_system_get("LocalCache", "image", "20225578-2019");
//读取缩放
        Cyber_Public_Var.WebZoom = csp.cwpd_system_get("APP", "WebZoom", "20225578-2019");
        //2020-7-9 读取资源
        Cyber_Public_Var.App_localcache_resource = csp.cwpd_system_get("LocalCache", "resource", "20225578-2019");
        Cyber_Public_Var.App_localcache_resource_path = csp.cwpd_system_get("LocalCache", "resource_path", "20225578-2019");
        Cyber_Public_Var.App_localcache_resource_path = Cyber_Public_Var. getCyberWinPath(Cyber_Public_Var.App_localcache_resource_path,this);
//主页
        //2022-6-14 app本地
        Cyber_Public_Var.App_local_path=Cyber_Public_Var. getCyberWinPath(Cyber_Public_Var.App_local_path,this);

        Cyber_Public_Var.App_localcache_DefaultHome_Load_From=csp.cwpd_system_get("LocalCache", "DefaultHome_Load_From", "20225578-2019");
        Cyber_Public_Var.NoNetworkDefaultErrorFrom=csp.cwpd_system_get("LocalCache", "NoNetworkDefaultErrorFrom", "20225578-2019");
        //2022-11-5  app微型app
        Cyber_Public_Var.CyberWin_MicroApp_local_path=Cyber_Public_Var. getCyberWinPath(Cyber_Public_Var.CyberWin_MicroApp_local_path,this);



        if(Cyber_Public_Var.App_localcache_DefaultHome_Load_From.equals("set")){
            //来自系统配置，不操作
        }else{
            String CyberWinPHP_Path=  Cyber_Public_Var.App_localcache_resource_path;//  Cyber_Public_Var. getCyberWinPath(Cyber_Public_Var.App_localcache_resource_path
            String url_md5= "cyberwin"+Cyber_Public_Var.cyber_md5("cyberv20200709v3comynwlzccncyberwinserver");;//Cyber_Public_Var.cyber_md5("cyberv20200709v3comynwlzccncyberwinserver");
            String url_type="html";

            String file_cache_path_onename = url_md5 + "." + url_type;
            String cacahehhomeurl="file://external_files/"+CyberWinPHP_Path+file_cache_path_onename;
            Cyber_Public_Var.Application_StartupPath_Home=cacahehhomeurl;
            LogToFile.d_windows("卷宗","缓存","访问地址"+ cacahehhomeurl);
        }
    }


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"
    };

    public void verifyStoragePermissions() {
        // 检查当前权限
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission!= PackageManager.PERMISSION_GRANTED) {
            // 没有权限，请求权限
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }
    //2024-12-02
    @Override
    public void onBackPressed() {
        // 在这里添加您的拦截逻辑
       // Toast.makeText(this, "返回键被拦截", Toast.LENGTH_SHORT).show();
      //  super.onBackPressed();
        showExitDialog();
    }

    private void showExitDialog() {
//cyberwin_transparentdialogtheme
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.TransparentDialogTheme);
        builder.setTitle("屏触科技")
                .setMessage("是否退出客户端？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击确定按钮，不做任何操作
                        Cyber_Public_Var.m_cpu.onDestroy();
                        // 在这里添加退出应用的相关代码
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 点击取消按钮的处理逻辑
                // 确定按钮的处理逻辑
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();



// 获取对话框的窗口对象
        /*
        Window window = dialog.getWindow();
        if (window!= null) {
            // 设置窗口的宽度参数
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.width = 520;//WindowManager.LayoutParams.MATCH_PARENT;
            // 或者设置为固定的像素值，如 500
            window.setAttributes(layoutParams);
        }
*/
        dialog.show();
        //dialog.show();
    }

    public void 未来之窗_人工智能_客户首页(){
        String HOME_布草厂="http://51.onelink.ynwlzc.net/o2o/wap.php?g=Wap&c=Card_new_membersManager&a=index_Hotel_Linen_byarea";
        String HOME_报销 = "http://51.onelink.ynwlzc.net/o2o/wap.php?g=Wap&c=Merchant_Manager_Report_BookkeepingUser&a=store_inout_apply";
        String HOME_酒店押金 = "http://51.onelink.ynwlzc.net/o2o/wap.php?g=Wap&c=Merchant_Manager_hotel&a=hotel_deposit_light_v2024";


        String HOME_万能押金单20240709 = "http://yj.ynwlzc.cn/cyberwin_w.php?g=Wap&c=Merchant_Manager_CommonDeposit&a=deposit_order_manager_Simplified";


        //  cwpd_Web.loadUrl(HOME_布草厂);
        // cwpd_Web.loadUrl(HOME_报销);
        HOME_酒店押金 ="http://yj.ynwlzc.cn/cyberwin_w.php?g=Wap&c=Merchant_Manager_hotel&a=hotel_deposit_light_v2024";
//http://51.onelink.ynwlzc.net/o2o/index.php/startlink/1
        String HOME_pos工作台20240802 = "http://51.onelink.ynwlzc.net/o2o/index.php/startlink/1";
        //http://51.onelink.ynwlzc.net/o2o/pos.php?g=Pos&c=Workbench_Workflow&a=index_shopnewpc
        String HOME_pos工作台屏触20240802 = "http://51.onelink.ynwlzc.net/o2o/pos.php?g=Pos&c=Workbench_Workflow&a=index_shopnewpc";

        String HOME_pos未来之窗手机工作台20240828 = "http://51.onelink.ynwlzc.net/o2o/wap.php?g=Wap&c=Merchant_Workflow";

        String HOME_pos未来之窗手VOSIDE20240914= "file:///android_asset/app/CyberTrade_ecogen_VOS_Studio/apps_codeeditor_ace.html";
      //
        String HOME_未来之窗手VOSIDE20240914= "file:///android_asset/app/CyberTrade_ecogen_VOS_Studio/cybewinapp.html";
        String HOME_屏触客户端= "http://pc.ynwlzc.net/xf_client/v202401/hcacolapp.html";
//http://51.onelink.ynwlzc.net/o2o/wap.php?g=Wap&c=Merchant_Manager_RandomSelectSystem&a=elections_withcat_minpro
        String HOME_suiji= "http://51.onelink.ynwlzc.net/o2o/wap.php?g=Wap&c=Merchant_Manager_RandomSelectSystem&a=elections_withcat_minpro";

        // cwpd_Web.loadUrl(HOME_酒店押金);
        // cwpd_Web.loadUrl(HOME_万能押金单20240709);
      //  cwpd_Web.loadUrl(HOME_屏触客户端);
        //cwpd_Web.loadUrl(HOME_suiji);
        //  cwpd_Web.loadUrl(HOME_报销);
        String HOME_屏触客户端local= "file:///android_asset/app/smartscreen/hcacolapp.html";


        if (Cyber_Public_Var.App_start_mode.equals("仙盟app")) {
            if (Cyber_Public_Var.Application_StartupPath_Home.startsWith("http://")) {

            }
            if (Cyber_Public_Var.Application_StartupPath_Home.startsWith("https://")) {

            }
            if (Cyber_Public_Var.Application_StartupPath_Home.startsWith("cyberwin_app://")) {
                String App_name = Cyber_Public_Var.Application_StartupPath_Home.replaceFirst("cyberwin_app://", "");
                String App_local_path = Cyber_Public_Var.App_local_path;
                String App_loaclrunpath = App_local_path + "" + App_name + "/cybewinapp.html";
                Cyber_Public_Var.Application_StartupPath_Home = "file://external_files/" + App_loaclrunpath;

            }

            LogToFile.d_windows("仙盟","小世界", "系统-app模式" + Cyber_Public_Var.Application_StartupPath_Home);

            cwpd_Web.loadUrl(Cyber_Public_Var.Application_StartupPath_Home);
        }else{
            LogToFile.d_windows("仙盟","小世界", "系" + HOME_屏触客户端local);

            cwpd_Web.loadUrl(HOME_屏触客户端local);
        }
       //天大bug CyberWin_Tickets_Device.东方仙盟_测试出卡();
    }

    public void cyberwin_lyaoutbtn_cusapphome(View view) {
        未来之窗_人工智能_客户首页();
    }


    @Override
    protected void onResume() {
        /**
         * 设置为横屏
         */
        // if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
        //   setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //  }

        iniFile IR = new iniFile();
        //屏幕方向
        String ScreenOrientation = IR.GetINI("Device", "ScreenOrientation", "", "CyberWin").replace("%CyberWinPHP_Path%", Application_StartupPath);// Application.StartupPath + "/CyberWinPHP");;
        if (ScreenOrientation.equals("Auto")) {
            //不做配置
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
        if (ScreenOrientation.equals("Horizontal")) {
            //不做配置
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        if (ScreenOrientation.equals("Vertical")) {
            //不做配置
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onResume();

        //2023-4-4
        /*
        if (NFC_mAdapter2023 != null) {
            if (!NFC_mAdapter2023.isEnabled()) {
                showWirelessSettingsDialog();
            }
            NFC_mAdapter2023.enableForegroundDispatch(this, mPendingIntent, null, null);
            NFC_mAdapter2023.enableForegroundNdefPush(this, mNdefPushMessage);
        }
        */
    }

    public void cyberwin_lyaoutbtn_wlzcappcenter(View view) {
     //cyberwin_getnewpower_logout
        String HOME_app = "http://app.ynwlzc.net/";

        cwpd_Web.loadUrl(HOME_app);
    }
    //cyberwin_lyaoutbtn_setting
    public void cyberwin_lyaoutbtn_setting(View view) {
        //cyberwin_getnewpower_logout
        String HOME_app = "file:///android_asset/wlzc_cwpd_login.html";

        cwpd_Web.loadUrl(HOME_app);
    }
    //cyberwin_lyaoutbtn_appstart
    public void cyberwin_lyaoutbtn_appstart(View view) {
        //cyberwin_getnewpower_logout
        iniFile IR = new iniFile();
        String DefaultHome = IR.GetINI("APP", "DefaultHome", "", "CyberWin").replace("%CyberWinPHP_Path%", Application_StartupPath);// Application.StartupPath + "/CyberWinPHP");;

        String HOME_app = DefaultHome;

        cwpd_Web.loadUrl(HOME_app);
    }

    public void cyberwin_lyaoutbtn_wlzcmgrlogout(View view) {
        //cyberwin_getnewpower_logout
        String HOME_退出 = "http://51.onelink.ynwlzc.net/o2o/wap.php?g=Wap&c=Merchant_Manager_hotel&a=cyberwin_getnewpower_logout";

        cwpd_Web.loadUrl(HOME_退出);
    }
    public void cyberwin_lyaoutbtn_changelefthide(View view) {
        //cyberwin_ly_toolbars
        LinearLayout 未来之窗工具栏目 = findViewById(R.id.cyberwin_ly_toolbars);
        LinearLayout.LayoutParams 未来之窗工具栏目params = (LinearLayout.LayoutParams)  未来之窗工具栏目 .getLayoutParams();
//设置宽度值
        未来之窗工具栏目params.width = dip2px(this, 18);
//设置高度值
      //  未来之窗工具栏目params.height = dip2px(this, 100);
//使设置好的布局参数应用到控件

        if(未来之窗_左侧工具栏_显示 == false){
            未来之窗工具栏目params.width = dip2px(this, 65);
            未来之窗_左侧工具栏_显示=true;
        }else{
            未来之窗工具栏目params.width = dip2px(this, 18);
            未来之窗_左侧工具栏_显示=false;
        }
        未来之窗工具栏目.setLayoutParams(未来之窗工具栏目params);



    }


    @JavascriptInterface
    public String wlzc_softhost_control( String action,String password) {
        if(action.equals("hideleftmenu")==true){
           未来之窗_左侧工具栏_隐藏();
            return "pc隐藏设置";
        }
        if(action.equals("showleftmenu")==true){
           未来之窗_左侧工具栏_展开();
            return "pc显示设置";
        }
        return "错误命令";


    }

    public   void 未来之窗_左侧工具栏_隐藏(){
        LinearLayout 未来之窗工具栏目 = findViewById(R.id.cyberwin_ly_toolbars);
        LinearLayout.LayoutParams 未来之窗工具栏目params = (LinearLayout.LayoutParams)  未来之窗工具栏目 .getLayoutParams();
//设置宽度值
        未来之窗工具栏目params.width = dip2px(this, 18);
        未来之窗工具栏目.setLayoutParams(未来之窗工具栏目params);
    }
    public   void 未来之窗_左侧工具栏_展开(){
        LinearLayout 未来之窗工具栏目 = findViewById(R.id.cyberwin_ly_toolbars);
        LinearLayout.LayoutParams 未来之窗工具栏目params = (LinearLayout.LayoutParams)  未来之窗工具栏目 .getLayoutParams();
//设置宽度值
        未来之窗工具栏目params.width = dip2px(this, 65);
        未来之窗工具栏目.setLayoutParams(未来之窗工具栏目params);
    }

    private  Boolean 未来之窗_左侧工具栏_显示=true;
    public void cyberwin_lyaoutbtn_leftshownow(View view) {
        //cyberwin_ly_toolbars
        LinearLayout 未来之窗工具栏目 = findViewById(R.id.cyberwin_ly_toolbars);
        LinearLayout.LayoutParams 未来之窗工具栏目params = (LinearLayout.LayoutParams)  未来之窗工具栏目 .getLayoutParams();
//设置宽度值
        未来之窗工具栏目params.width = dip2px(this, 65);
//设置高度值
      //  未来之窗工具栏目params.height = dip2px(this, 400);
//使设置好的布局参数应用到控件
        未来之窗_左侧工具栏_显示=true;

        未来之窗工具栏目.setLayoutParams(未来之窗工具栏目params);
    }
    /**
     * dp转为px
     *
     * @param context  上下文
     * @param dipValue dp值
     * @return
     */
    private int dip2px(Context context, float dipValue) {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
    }

    //cyberwin_lyaoutbtn_leftshownow
    //cyberwin_lyaoutbtn_changeleftshow

//cyberwin_lyaoutbtn_wlzcappcenter
    public void cyberwin_lyaoutbtn_exitapp(View view) {
        //未来之窗退出
       finish();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
       // mControlsView.setVisibility(View.GONE);
      //  mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
     //   mHideHandler.removeCallbacks(mShowPart2Runnable);
    //    mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        // Show the system bar
      // mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    //            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
      //  mVisible = true;

        // Schedule a runnable to display UI elements after a delay
      //  mHideHandler.removeCallbacks(mHidePart2Runnable);
      //  mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    /**
     * 初始化View
     */
    private void initView() {
        //  mTitle = (TextView) findViewById(R.id.title);
        //  mProgress = (ProgressBar) findViewById(R.id.Progress);
        cwpd_Web = (WebView) findViewById(R.id.cwpd_webview);
        //  mMaterialRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.refresh);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("JavascriptInterface")
    private void initWebView_Plugs() {
        // 修改ua使得web端正确判断
        String ua = cwpd_Web.getSettings().getUserAgentString();
        //设置允许跨域访问
        cwpd_Web.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        cwpd_Web.getSettings().setUserAgentString(ua + ";CyberWin WebBrowser/Android4.2020,2024");

        cwpd_Web.addJavascriptInterface(new Cyber_JsPrinterStandard(this, this), "Cyber_JsPrinterStandard"); //在JSHook类里实现javascript想调用的方法，并将其实例化传入webview,

        cwpd_Web.addJavascriptInterface(new Cyber_JsPrinterStandard(this, this), "CyberWin_JsStandardPlug"); //在JSHook类里实现javascript想调用的方法，并将其实例化传入webview,

//Cyber_JsVOS_Studio
        cwpd_Web.addJavascriptInterface(new Cyber_JsVOS_Studio(this, this), "CyberWin_VOS_Studio"); //在JSHook类里实现javascript想调用的方法，并将其实例化传入webview,

        cwpd_Web.addJavascriptInterface(new FullscreenActivity(), "CyberWin_HcaColVOS_PC"); //在JSHook类里实现javascript想调用的方法，并将其实例化传入webview,

        //cwpd_Web.setWebContentsDebuggingEnabled(true);
       // initlocalcache_kernel();//加载缓存配置

       // _initDpi();
        //2020-6-28 视频硬件加速
        cwpd_Web.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        //进入界面weview中的音视频不能自动播放 取消用户交互
       // cwpd_Web.getSettings().setMediaPlaybackRequiresUserGesture(false);



        cwpd_Web.getSettings().setDomStorageEnabled(true);
        cwpd_Web.getSettings().setDatabaseEnabled(true);
        //cwpd_Web.getSettings().set(true);

       // WebSettings webSettings = webView.getSettings();
        //2024-09-15 跨域访问
        cwpd_Web.getSettings().setAllowUniversalAccessFromFileURLs(true);
        cwpd_Web.getSettings().setAllowFileAccessFromFileURLs(true);
//2024-12-10
        cwpd_Web.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        //解决白屏问题，原因不明
        cwpd_Web.getSettings().setDomStorageEnabled(true);
    	/*
    	 * 1.Java调用js代码
    	 *

String call = "javascript:sumToJava(1,2)";
webView.loadUrl(call);
js调用Java

　　格式为：window.jsInterfaceName.methodName(parameterValues)

Java（webView）调用JS

　　格式为：webView.loadUrl(“javascript:methodName(parameterValues)”)
function sumToJava(number1, number2){
       window.control.onSumResult(number1 + number2)
}
    	 */

        cwpd_Web.getSettings().setUseWideViewPort(true);


// 启用宽视口模式，适应不同屏幕尺寸
        cwpd_Web.getSettings().setLoadWithOverviewMode(true);

// 使页面根据屏幕大小进行缩放和调整

        cwpd_Web.getSettings().setAllowUniversalAccessFromFileURLs(true);

// 允许从本地文件 URL 进行通用访问



        cwpd_Web.getSettings().setAllowFileAccessFromFileURLs(true);

// 允许从文件 URL 访问文件



        cwpd_Web.getSettings().setPluginState(WebSettings.PluginState.ON);

        cwpd_Web.getSettings().setMediaPlaybackRequiresUserGesture(false);

    }

    /**
     * 初始化WebView
     */
    private void initWebView() {
        //  cwpd_Web.


        cwpd_Web.getSettings().setJavaScriptEnabled(true);
        //禁止图片
        //cwpd_Web.getSettings().setBlockNetworkImage(true);
        cwpd_Web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        cwpd_Web.getSettings().setSupportMultipleWindows(true);

        //setAllowFileAccess

        // 设置允许访问文件数据
        //2023-11-8
        cwpd_Web.getSettings().setAllowFileAccess(true);
        cwpd_Web.getSettings().setAllowContentAccess(true);

        //非常重要
        cwpd_Web.setWebViewClient(new Cyber_WebviewClient());
        cwpd_Web.setWebChromeClient(new Cyber_ChromeClient());

    }




    public class Cyber_WebviewClient extends WebViewClient {
        private String errorHtml = "<html>未来之窗悠然CWPD核心</html>";

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //不知道为什么要删掉你  view.loadUrl(url);
            //view.loadUrl(url);
            // return true;
            // return false;// false 显示frameset, true 不显示Frameset
            // 如下方案可在非微信内部WebView的H5页面中调出微信支付

            if (url.startsWith("weixin://wap/pay?")) {

                Intent intent = new Intent();

                intent.setAction(Intent.ACTION_VIEW);

                intent.setData(Uri.parse(url));

                startActivity(intent);
                return true;

            }
//支付宝
            if (url.startsWith("alipays://")) {

                Intent intent = new Intent();

                intent.setAction(Intent.ACTION_VIEW);

                intent.setData(Uri.parse(url));

                startActivity(intent);
                return true;

            }




            return false;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {


            String url_md5= Cyber_Public_Var.cyber_md5(url);
            String url_type= Cyber_Public_Var. cyber_getFileExt(url);
            // LogToFile.d("资源池子", url);
            // filenameV

            try {
                Uri 未来之窗url = Uri.parse((String) url);
                String 未来之窗协议 = 未来之窗url.getScheme();
                String 未来之窗主机 = 未来之窗url.getHost();
                switch (未来之窗协议) {
                    case "cyberwinmicroapp": {
                        //未来之窗转移执行
                        return CyberWin_MicroAPP_Android.cyberwin_microapp_提取资源(url);
                    }
                    // break;
                    case "cyberwinmicrores": {
                        //未来之窗转移执行
                        return CyberWin_MicroAPP_Android.cyberwin_microapp_提取资源(url);

                        //Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();
                        // Cyber_Zip
                    }
                /*
                default:
                {

                }
                break;
                */
                }
            }catch (Exception ex){
                LogToFile.d_windows("未来之窗微内核异常","微内核异常",ex.getMessage());
            }

            //2022-7-9 将互联网资源替换为本地资源
            try {
                //根本不考虑缓存
                if (url.indexOf("cyberwin_appres://")>-1){
                    LogToFile.d_windows("未来之窗内核","资源",url);
                }else{
                    // LogToFile.d_windows("未来之窗内核","资源不匹配",url);
                }
                if (url.indexOf("cyberwin_appres://jquery-1.8.3.min.js")>-1) {
                    String mimetype = "text/javascript";
                    InputStream reader = cyber_instance.getAssets().open("app/js/jquery-1.8.3.min.js");
                    WebResourceResponse response2 = new WebResourceResponse(mimetype, "UTF-8", reader);
                    return response2;
                }

                if (url.indexOf("cyberwin_appres://cyberwin_jquery")>-1) {
                    String mimetype = "text/javascript";
                    InputStream reader = cyber_instance.getAssets().open("app/js/cyberwin_javascript_query.js");
                    WebResourceResponse response2 = new WebResourceResponse(mimetype, "UTF-8", reader);
                    return response2;
                }

                if (url.indexOf("cyberwin_appres://cyberwin_apploader")>-1) {
                    String mimetype = "text/javascript";

                    // InputStream in = mContext.getAssets().open(DB_NAME);
                    InputStream reader = cyber_instance.getAssets().open("app/js/cyberwin_apploader.js");
                    WebResourceResponse response2 = new WebResourceResponse(mimetype, "UTF-8", reader);
                    return response2;
                }

                if (url.indexOf("cyberwin_appres://cyberwin_hardware_sync_smartlockers")>-1) {
                    String mimetype = "text/javascript";
                    InputStream reader = cyber_instance.getAssets().open("/app/js/cyberwin_hardware_sync_smartlockers.js");
                    WebResourceResponse response2 = new WebResourceResponse(mimetype, "UTF-8", reader);
                    return response2;
                }

            }catch (Exception ex){
                LogToFile.d_windows("系统异常","本地资源",ex.getMessage());
            }


            WebResourceResponse response = null;

            // return response;

            return super.shouldInterceptRequest(view, url);

        }



        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            // mProgress.setVisibility(View.VISIBLE);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // mProgress.setVisibility(View.GONE);
            //加载后，自动播放
            //2020-6-29
            view.loadUrl("javascript:try{cwpd_autoplay();}catch(e){}");
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

            String 未来之窗错误="<title>未来之窗智慧管理</title>";
            未来之窗错误=未来之窗错误+"<h1>未来之窗VOS</h1>";
            未来之窗错误=未来之窗错误+"系统位置错误：";
            未来之窗错误=未来之窗错误+"<br>errorCode："+errorCode;
            未来之窗错误=未来之窗错误+"<br>description："+description;

            view.loadData(未来之窗错误, "text/html", "UTF-8");

            //Log.i(TAG, "-MyWebViewClient->onReceivedError()--\n errorCode="+errorCode+" \ndescription="+description+" \nfailingUrl="+failingUrl);
            //这里进行无网络或错误处理，具体可以根据errorCode的值进行判断，做跟详细的处理。
            //  view.loadData(errorHtml, "text/html", "UTF-8");
            /*
            LogToFile.d("Exception", "failingUrl" + failingUrl + " errorCode =" + errorCode);
            LogToFile.d("wv异常","异常地址"+ failingUrl+" errorCode:"+errorCode+" description:"+description);
            //  view.loadUrl("file:///android_asset/wlzc_cwpd_error.html" + "?url=" + failingUrl);

            // Cyber_Public_Var.NoNetworkDefaultErrorFrom=csp.cwpd_system_get("LocalCache", "NoNetworkDefaultErrorFrom", "20225578-2019");
            //
            //
            //
            LogToFile.d("未来之窗地址异常", "当前访问地址" + failingUrl);

            if(Cyber_Public_Var.NoNetworkDefaultErrorFrom.equals("set")) {
                view.loadUrl("file:///android_asset/wlzc_cwpd_error.html" + "?url=" + failingUrl);

            }else if(Cyber_Public_Var.NoNetworkDefaultErrorFrom.equals("Local")){
                String CyberWinPHP_Path=  Cyber_Public_Var.App_localcache_resource_path;//  Cyber_Public_Var. getCyberWinPath(Cyber_Public_Var.App_localcache_resource_path
                String url_md5= "cyberwin"+Cyber_Public_Var.cyber_md5("cyberv20200709v3comynwlzccncyberwinserver");;//Cyber_Public_Var.cyber_md5("cyberv20200709v3comynwlzccncyberwinserver");
                String url_type="html";

                String file_cache_path_onename = url_md5 + "." + url_type;
                String cacahehhomeurl="file://external_files/"+CyberWinPHP_Path+file_cache_path_onename;
                File f=new File(CyberWinPHP_Path+file_cache_path_onename);
                if(!f.exists()) {
                    view.loadUrl("file:///android_asset/wlzc_cwpd_login.html");
                    LogToFile.d("本地主页","不存在本地主页");
                }else {
                    LogToFile.d("本地主页","找到本地主页");

                    view.loadUrl(cacahehhomeurl);
                }


            }else if(Cyber_Public_Var.NoNetworkDefaultErrorFrom.equals("defaulterror")){
                view.loadUrl("file:///android_asset/wlzc_cwpd_error.html" + "?url=" + failingUrl);
            }else if(Cyber_Public_Var.NoNetworkDefaultErrorFrom.equals("login")){
                view.loadUrl("file:///android_asset/wlzc_cwpd_login.html");
            }
            */




        }
        //2024-09-15
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            // 忽略 HTTP 错误，允许跨域访问
            int statusCode = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                statusCode = errorResponse.getStatusCode();
            }

            // 假设您要忽略 403 跨域错误
            if (statusCode == 403 && isCrossOriginError(errorResponse)) {
                return;
            }
            if ( isCrossOriginError(errorResponse)) {
                return;
            }
            return;
        }
        private boolean isCrossOriginError(WebResourceResponse errorResponse) {
            // 根据错误响应的某些特征判断是否为跨域错误
            // 例如检查响应头中的特定字段
            String header = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                header = errorResponse.getResponseHeaders().get("SomeHeaderIndicatingCrossOrigin");
            }
            return header!= null && header.equals("SomeValue");
        }
    }

    /**
     * 重写MyChromeClient方法
     * <p>
     * onProgressChanged（） 设置动态进度条
     * onReceivedTitle（） 设置WebView的头部标题
     * onReceivedIcon（）  设置WebView的头部图标
     */
    private class Cyber_ChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            //  mProgress.setProgress(newProgress);

        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            // mTitle.setText(title);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {
            new AlertDialog.Builder(cyber_instance)
                    .setTitle("悠然智慧管理4.0")
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok,
                            new AlertDialog.OnClickListener() {


                                public void onClick(DialogInterface arg0, int arg1) {
                                    // TODO Auto-generated method stub
                                    result.confirm();

                                }
                            }).setCancelable(false).create().show();

            return true;
        }

        //2020-5-15

        /**
         * 对网络连接状态进行判断
         * @return  true, 可用； false， 不可用
         */
        private boolean isOpenNetwork() {
            ConnectivityManager connManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();     if(networkInfo!= null) {
                //2.获取当前网络连接的类型信息
                //
                int networkType = networkInfo.getType();
                if(ConnectivityManager.TYPE_WIFI == networkType){
                    //当前为wifi网络
                }else if(ConnectivityManager.TYPE_MOBILE == networkType){
                    //当前为mobile网络
                }
                return connManager.getActiveNetworkInfo().isAvailable(); }

            return false;
        }

        /**
         * 覆盖默认的window.confirm展示界面，避免title里显示为“：来自file:////”
         */
        public boolean onJsConfirm(WebView view, String url, String message,
                                   final JsResult result) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("优然智慧管理系统")
                    .setMessage(message)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    })
                    .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.cancel();
                        }
                    });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    result.cancel();
                }
            });

            // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    //Log.v("onJsConfirm", "keyCode==" + keyCode + "event="+ event);
                    return true;
                }
            });
            // 禁止响应按back键的事件
            // builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
            // return super.onJsConfirm(view, url, message, result);
        }

        /**
         * 覆盖默认的window.prompt展示界面，避免title里显示为“：来自file:////”
         * window.prompt('请输入您的域名地址', '618119.com');
         */
        public boolean onJsPrompt(WebView view, String url, String message,
                                  String defaultValue, final JsPromptResult result) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());


            builder.setTitle("优然智慧管理系统").setMessage(message);

            final EditText et = new EditText(view.getContext());
            et.setSingleLine();
            et.setText(defaultValue);


            builder.setView(et)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm(et.getText().toString());


                        }

                    })
                    .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.cancel();
                        }
                    });

            // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    //Log.v("onJsPrompt", "keyCode==" + keyCode + "event="+ event);
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        result.confirm(et.getText().toString());
                        dialog.dismiss();
                        return true;

                    }else{
                        return false;
                    }

                    //
                }
            });

            // 禁止响应按back键的事件
            // builder.setCancelable(false);
            final  AlertDialog    dialog = builder.create();

            //2021-3-18 回车检测
            et.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        // 监听到回车键，会执行2次该方法。按下与松开
                        result.confirm(et.getText().toString());
                        dialog.dismiss();

                    }
                    return false;

                }
            });

            dialog.show();
            return true;
            // return super.onJsPrompt(view, url, message, defaultValue,
            // result);
        }
        /////////2020-5-15


        //20240604 文件
        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri[]> valueCallback) {
           // uploadMessage = valueCallback;
           // openImageChooserActivity();
            if (mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(null);
            }
            mFilePathCallback = valueCallback;

            Intent intent = 未来之窗_人工智能_拍照和文件ChooseFile();  // 选择文件及拍照
            startActivityForResult(intent, REQUEST_CODE_LOLIPOP);
           // return true;
        }

        // For Android  >= 3.0
        public void openFileChooser(ValueCallback valueCallback, String acceptType) {
            //uploadMessage = valueCallback;
          //  openImageChooserActivity();
            if (mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(null);
            }
            mFilePathCallback = valueCallback;

            Intent intent = 未来之窗_人工智能_拍照和文件ChooseFile();  // 选择文件及拍照
            startActivityForResult(intent, REQUEST_CODE_LOLIPOP);
        }

        //For Android  >= 4.1
        public void openFileChooser(ValueCallback<Uri[]> valueCallback, String acceptType, String capture) {
           // uploadMessage = valueCallback;
           // openImageChooserActivity();
            if (mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(null);
            }
            mFilePathCallback = valueCallback;
            Intent intent = 未来之窗_人工智能_拍照和文件ChooseFile();  // 选择文件及拍照
            startActivityForResult(intent, REQUEST_CODE_LOLIPOP);
        }

        // For Android >= 5.0
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
           // uploadMessageAboveL = filePathCallback;
           // openImageChooserActivity();
            if (mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(null);
            }
            mFilePathCallback = filePathCallback;
            Intent intent = 未来之窗_人工智能_拍照和文件ChooseFile();  // 选择文件及拍照
            startActivityForResult(intent, REQUEST_CODE_LOLIPOP);
            return true;
        }


        //2024-09-15
        //@Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            // 忽略 HTTP 错误，允许跨域访问
            int statusCode = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                statusCode = errorResponse.getStatusCode();
            }

            // 假设您要忽略 403 跨域错误
            if (statusCode == 403 && isCrossOriginError(errorResponse)) {
                return;
            }
            if ( isCrossOriginError(errorResponse)) {
                return;
            }
            return;
        }
        private boolean isCrossOriginError(WebResourceResponse errorResponse) {
            // 根据错误响应的某些特征判断是否为跨域错误
            // 例如检查响应头中的特定字段
            String header = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                header = errorResponse.getResponseHeaders().get("SomeHeaderIndicatingCrossOrigin");
            }
            return header!= null && header.equals("SomeValue");
        }




    }

    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    private final static int FILE_CHOOSER_RESULT_CODE = 10000;

    // 2.回调方法触发本地选择文件
    private void openImageChooserActivity() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
//        i.setType("image/*");//图片上传
//        i.setType("file/*");//文件上传
        i.setType("*/*");//文件上传
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
    }
    // 3.选择图片后处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //2024-6-9
        if (requestCode == REQUEST_CODE_LOLIPOP) {  // 选择文件返回 5.0+
            Uri[] results = null;
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    if (mCameraPhotoPath != null) {
                      //  Toast.makeText(Cyber_Public_Var.cyber_main_instance, "拍照返回"+mCameraPhotoPath, Toast.LENGTH_SHORT).show();

                        String 未来之窗人工智能转换_安全str前 =mCameraPhotoPath.replace("file://","");
                        Uri  未来之窗人工智能转换_安全  = 未来之窗_人工智能_文件安全转换url(Cyber_Public_Var.m_cpu, 未来之窗人工智能转换_安全str前);
                        Uri  未来之窗人工智能转换_安全2 =    未来之窗_人工智能_文件安全转换url_二次(未来之窗人工智能转换_安全);

                        Toast.makeText(Cyber_Public_Var.cyber_main_instance, "拍照返回"+mCameraPhotoPath+"未来之窗安全"+未来之窗人工智能转换_安全.toString()+"未来之窗人工智能转换_安全2="+未来之窗人工智能转换_安全2, Toast.LENGTH_SHORT).show();

                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                       // results = new Uri[]{未来之窗人工智能转换_安全2};
                    }
                } else {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        Toast.makeText(Cyber_Public_Var.cyber_main_instance, "选择返回"+dataString, Toast.LENGTH_SHORT).show();

                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }
            mFilePathCallback.onReceiveValue(results);  // 当获取要传图片的Uri，通过该方法回调通知
            mFilePathCallback = null;
        }

        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == uploadMessage && null == uploadMessageAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            // Uri result = (((data == null) || (resultCode != RESULT_OK)) ? null : data.getData());
            if (uploadMessageAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(result);
                uploadMessage = null;
            }
        } else {
            //这里uploadMessage跟uploadMessageAboveL在不同系统版本下分别持有了
            //WebView对象，在用户取消文件选择器的情况下，需给onReceiveValue传null返回值
            //否则WebView在未收到返回值的情况下，无法进行任何操作，文件选择器会失效
            if (uploadMessage != null) {
                uploadMessage.onReceiveValue(null);
                uploadMessage = null;
            } else if (uploadMessageAboveL != null) {
                uploadMessageAboveL.onReceiveValue(null);
                uploadMessageAboveL = null;
            }
        }
    }

    // 4. 选择内容回调到Html页面
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || uploadMessageAboveL == null)
            return;
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        uploadMessageAboveL.onReceiveValue(results);
        uploadMessageAboveL = null;
    }

    //2024-09-15
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getAction().equals(Intent.ACTION_VIEW)) {
            Uri uri = intent.getData();
            // 处理文件 uri 的逻辑
            //''
            Toast.makeText(Cyber_Public_Var.cyber_main_instance, "快捷方式:"+uri, Toast.LENGTH_SHORT).show();

        }
    }
//未来之窗接受数据
    /*
@Override
public int onStart(Intent intent, int flags, int startId) {
    if (Intent.ACTION_VIEW.equals(intent.getAction())) {
        Uri uri = intent.getData();
        if (uri!= null) {
            // 这里可以进行对自定义数据的查看相关操作，例如解析数据等
            String data = uri.getQueryParameter("data");
            // Log.d("ViewService", "Received data: " + data);
        }
    }
    return super.onStart(intent, flags, startId);
}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            if (uri!= null) {
                // 这里可以进行对自定义数据的查看相关操作，例如解析数据等
                String data = uri.getQueryParameter("data");
               // Log.d("ViewService", "Received data: " + data);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
    */

    //2024-6-9 未来之窗 14:47
    private int REQUEST_CODE_LOLIPOP = 1;  // 5.0以上版本
    private String mCameraPhotoPath = "";  // 拍照的图片路径
    private ValueCallback<Uri[]> mFilePathCallback = null;
    /**
     * 选择文件及拍照
     */
    private Intent 未来之窗_人工智能_拍照和文件ChooseFile() {
        String saveName = Environment.getExternalStorageDirectory().getPath() + "/" + Environment.DIRECTORY_DCIM + "/Camera/";

        /**
         * 打开相机intent
         */
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            File photoFile = null;
            photoFile = new File(saveName + randomFileName() + ".jpg");
            if (!photoFile.exists()) {
                //file:// 拍照返回
               // mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
               // mCameraPhotoPath = "file://" + photoFile.getAbsolutePath();
              //  Uri  未来之窗人工智能转换_安全  = 未来之窗_人工智能_文件安全转换url(Cyber_Public_Var.m_cpu, photoFile.getAbsolutePath());
              //  Uri  未来之窗人工智能转换=Uri.fromFile(photoFile);
             //   mCameraPhotoPath=未来之窗人工智能转换.toString();

              //  Uri contentUri = FileProvider.getUriForFile(FullscreenActivity.this,
//                        "com.ynwlzc.framework.cyberwinosand.cyberwinFileProvider", photoFile);
               // mCameraPhotoPath =contentUri.toString();
                mCameraPhotoPath= Uri.fromFile(photoFile).toString();
                //file://
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));  // 把Uri赋值给takePictureIntent
            } else {
                takePictureIntent = null;
            }
        }

        Intent[] takeoutArray = null;
        if (takePictureIntent != null) {
            takeoutArray = new Intent[]{takePictureIntent};
        } else {
            takeoutArray = new Intent[0];
        }

        /**
         * 获取图片intent
         */
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");

        /**
         * 使用系统选择器
         */
        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, takeoutArray);  // 额外的intent

        return chooserIntent;
    }

    /**
     * 随机产生文件名
     */
    private String randomFileName() {
      //  UUID deviceUuid = new UUID(ANDROID_ID.hashCode(), ((long) ANDROID_ID.hashCode() << 32));
        return UUID.randomUUID().toString();
    }


    //
    //路径文件转成URI
    public static Uri getImageContentUri(Context context, java.io.File imageFile) {

        String filePath = imageFile.getAbsolutePath();

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,

                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",

                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {

            @SuppressLint("Range")
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));

            Uri baseUri = Uri.parse("content://media/external/images/media");

            return Uri.withAppendedPath(baseUri, "" + id);

        } else {

            if (imageFile.exists()) {

                ContentValues values = new ContentValues();

                values.put(MediaStore.Images.Media.DATA, filePath);

                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            } else {

                return null;

            }
        }
    }

    //路径文件转成URI
    public static Uri 未来之窗_人工智能_文件安全转换url_二次(Uri 未来之窗一级) {
        String 未来之窗一级str = 未来之窗一级.toString();
        未来之窗一级str=未来之窗一级str.replace("content://media/external/images/media","content://com.android.providers.media.documents/document/image");

        return Uri.parse(未来之窗一级str);


    }
        //路径文件转成URI
    public static Uri 未来之窗_人工智能_文件安全转换url(Context context, String filePath) {

       // String filePath = imageFile.getAbsolutePath();

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,

                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",

                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {

            @SuppressLint("Range")
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));

            Uri baseUri = Uri.parse("content://media/external/images/media");
            Uri baseUri未来之窗 = Uri.parse("content://com.android.providers.media.documents/document/image%3A");

            return Uri.withAppendedPath(baseUri未来之窗, "" + id);

        } else {

           // if (imageFile.exists()) {

                ContentValues values = new ContentValues();

                values.put(MediaStore.Images.Media.DATA, filePath);

                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

           // } else {

            //    return null;

            //}
        }
    }

    //2024-12-03
    private void StartLocalServer() {

        try {
            //  new Cyber_Local_Server();
            //取消老
            Cyber_Server_Web myServer = new Cyber_Server_Web(20251);
            myServer.start();
            //   Cyber_Server_Web.st
            //  try {
            //  myServer.start();
            //} catch (IOException e) {
            //  e.printStackTrace();
            //}

        } catch (Exception ioe) {
            LogToFile.d_windows("网络服务","网络服务", "启动失败Couldn't start server" + ioe.getMessage());
            // System.err.println("Couldn't start server:\n" + ioe);
            // System.exit(-1);
        }

    }


    public   void 东方仙盟_冥界_传送阵(String 灵舟 ,String 灵体) {
        //nfc_reader
        String 未来之窗反向js2 = "javascript:" + "cyberWin_Device_AIOT_Monitor" + "(\""+灵舟+"\",`" + 灵体 + "`)";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            cwpd_Web.evaluateJavascript(未来之窗反向js2, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            // LogUtil.e(TAG, "getPayId onReceiveValue " + functionName);
                        }
                    }
            );
        }
    }



}
