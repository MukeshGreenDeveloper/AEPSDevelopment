package softwarepassanga.aeps1;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import softwarepassanga.aeps1.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    String sGlobalPID;
    public static final String DEVICE = "DEVICE";
//    GetBiometricDeviceMappingbycustomerId getBiometricDeviceMappingbycustomerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.getBiometricDeviceMappingbycustomerId = (GetBiometricDeviceMappingbycustomerId)
//                getIntent().getExtras().getParcelable(DEVICE);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
//                getBiometricDeviceMappingbycustomerId = (GetBiometricDeviceMappingbycustomerId)
//                        getIntent().getExtras().getParcelable(DEVICE);
                captureFingerPrint();
            }
        });
    }

    private int STARTEK_REQUEST = 71;

    private void captureFingerPrint() {
        try {
//            String lowerCase = this.getBiometricDeviceMappingbycustomerId.getmDeviceName().toLowerCase();

            if (appInstalledOrNot("com.acpl.registersdk")) {
                Intent intent2 = new Intent("in.gov.uidai.rdservice.fp.CAPTURE", (Uri) null);
                intent2.putExtra("PID_OPTIONS", createPidOptXML());
                intent2.setPackage("com.acpl.registersdk");
                startActivityForResult(intent2, this.STARTEK_REQUEST);
                return;
            }
            openStartekOnPlayStore();
//            }
        } catch (Exception unused) {
            Toast.makeText(this, "Could not scan finger..", Toast.LENGTH_SHORT).show();
        }
    }

    private String createPidOptXML() {
        try {
            DocumentBuilderFactory newInstance = DocumentBuilderFactory.newInstance();
            newInstance.setNamespaceAware(true);
            Document newDocument = newInstance.newDocumentBuilder().newDocument();
            newDocument.setXmlStandalone(true);
            Element createElement = newDocument.createElement("PidOptions");
            newDocument.appendChild(createElement);
            Element createElement2 = newDocument.createElement("Opts");
            createElement.appendChild(createElement2);
            Attr createAttribute = newDocument.createAttribute("fCount");
            createAttribute.setValue(AppConstant.deviceType);
            createElement2.setAttributeNode(createAttribute);
            Attr createAttribute2 = newDocument.createAttribute("fType");
            createAttribute2.setValue("0");
            createElement2.setAttributeNode(createAttribute2);
            Attr createAttribute3 = newDocument.createAttribute("iCount");
            createAttribute3.setValue("0");
            createElement2.setAttributeNode(createAttribute3);
            Attr createAttribute4 = newDocument.createAttribute("iType");
            createAttribute4.setValue("");
            createElement2.setAttributeNode(createAttribute4);
            Attr createAttribute5 = newDocument.createAttribute("pCount");
            createAttribute5.setValue("0");
            createElement2.setAttributeNode(createAttribute5);
            Attr createAttribute6 = newDocument.createAttribute("pType");
            createAttribute6.setValue("");
            createElement2.setAttributeNode(createAttribute6);
            Attr createAttribute7 = newDocument.createAttribute("format");
            createAttribute7.setValue("0");
            createElement2.setAttributeNode(createAttribute7);
            Attr createAttribute8 = newDocument.createAttribute("pidVer");
            createAttribute8.setValue("2.0");
            createElement2.setAttributeNode(createAttribute8);
            Attr createAttribute9 = newDocument.createAttribute("timeout");
            createAttribute9.setValue("20000");
            createElement2.setAttributeNode(createAttribute9);
            Attr createAttribute10 = newDocument.createAttribute(AppConstant.otp);
            createAttribute10.setValue("");
            createElement2.setAttributeNode(createAttribute10);
            Attr createAttribute11 = newDocument.createAttribute("env");
            createAttribute11.setValue("P");
            createElement2.setAttributeNode(createAttribute11);
            Attr createAttribute12 = newDocument.createAttribute("wadh");
            createAttribute12.setValue("");
            createElement2.setAttributeNode(createAttribute12);
            Attr createAttribute13 = newDocument.createAttribute("posh");
            createAttribute13.setValue("UNKNOWN");
            createElement2.setAttributeNode(createAttribute13);
            Element createElement3 = newDocument.createElement("Demo");
            createElement3.setTextContent("");
            createElement.appendChild(createElement3);
            Element createElement4 = newDocument.createElement("CustOpts");
            createElement.appendChild(createElement4);
            Element createElement5 = newDocument.createElement("Param");
            createElement4.appendChild(createElement5);
            Attr createAttribute14 = newDocument.createAttribute("name");
            createAttribute14.setValue("ValidationKey");
            createElement5.setAttributeNode(createAttribute14);
            Attr createAttribute15 = newDocument.createAttribute("value");
            createAttribute15.setValue("ONLY USE FOR LOCKED DEVICES.");
            createElement5.setAttributeNode(createAttribute15);
            Transformer newTransformer = TransformerFactory.newInstance().newTransformer();
            newTransformer.setOutputProperty("standalone", "yes");
            DOMSource dOMSource = new DOMSource(newDocument);
            StringWriter stringWriter = new StringWriter();
            newTransformer.transform(dOMSource, new StreamResult(stringWriter));
            return stringWriter.getBuffer().toString().replaceAll("\n|\r", "").replaceAll("&lt;", "<").replaceAll("&gt;", ">");
        } catch (Exception unused) {
            return "";
        }
    }

    private void openStartekOnPlayStore() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Message");
        builder.setMessage("Please download ACPL app to use this service.");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.BROWSABLE");
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.acpl.registersdk&hl=en"));
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int i3 = 0;
        if (data != null) {
            Log.e("kesyssss", "onActivityResult: else " + data.toString());
            if (requestCode == this.STARTEK_REQUEST) {
                String stringExtra6 = data.getStringExtra("PID_DATA");
                if (stringExtra6 != null) {
                    Log.d("kesyssss", "onActivityResult: pidDataXML " + stringExtra6);
                    String obj2 = PIDXMLSerializer.getJsonFromXml(stringExtra6).toString();
                    Log.d("kesyssss", "onActivityResult: mJsonString " + obj2);
                    StarTekResponce starTekResponce = (StarTekResponce) new Gson().fromJson(obj2, new TypeToken<StarTekResponce>() {
                    }.getType());
                    if (Double.parseDouble(starTekResponce.getPiddata().getResp().getErrcode().toString()) == 0.0d) {
                        boolean z2 = false;
                        while (i3 < starTekResponce.getPiddata().getDeviceinfo().getAdditionalInfo().getParam().size()) {
                            if (starTekResponce.getPiddata().getDeviceinfo().getAdditionalInfo().getParam().get(i3).getName().toLowerCase().equals("srno")) {
//                                if (this.getBiometricDeviceMappingbycustomerId.getTerminalID().equals(StringUtils.leftPad(starTekResponce.getPiddata().getDeviceinfo().getAdditionalInfo().getParam().get(i3).getValue(), 15, "0"))) {
                                z2 = true;
                                Log.d("kesyssss", "=>>" + StringUtils.leftPad(starTekResponce.getPiddata().getDeviceinfo().getAdditionalInfo().getParam().get(i3).getValue(), 15, "0"));
//                                }
                            }
                            i3++;
                        }
                        if (z2) {
                            this.sGlobalPID = stringExtra6;
//                            showFinalDetails();
                            return;
                        }
                        showMessageDialogue("Device mismatch, please try again with configured device", "Message");
                        return;
                    }
                    showMessageDialogue(starTekResponce.getPiddata().getResp().getErrinfo(), "Message");
                    return;
                }
                showMessageDialogue("Could not scan finger print,Please try again.", "Message");
            }
        }
    }

    private boolean appInstalledOrNot(String str) {
        try {
            getPackageManager().getPackageInfo(str, 0);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    private void showMessageDialogue(String str, String str2) {
        new AlertDialog.Builder(this).setCancelable(false).setTitle((CharSequence) str2).setMessage((CharSequence) str).setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}