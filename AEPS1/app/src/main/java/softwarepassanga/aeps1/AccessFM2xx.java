package softwarepassanga.aeps1;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Date;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.hardware.usb.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.app.PendingIntent;

import softwarepassanga.startek.fm210.tstlib;


public class AccessFM2xx extends Activity {

	final int U_LEFT = -41; // NO_UCD (unused code)
	final int U_RIGHT = -42; // NO_UCD (unused code)
	final int U_UP = -43; // NO_UCD (unused code)
	final int U_DOWN = -44; // NO_UCD (unused code)
	final int U_POSITION_CHECK_MASK = 0x00002F00; // NO_UCD (unused code)
	final int U_POSITION_NO_FP = 0x00002000; // NO_UCD (unused code)
	final int U_POSITION_TOO_LOW = 0x00000100; // NO_UCD (use private)
	final int U_POSITION_TOO_TOP = 0x00000200; // NO_UCD (use private)
	final int U_POSITION_TOO_RIGHT = 0x00000400; // NO_UCD (use private)
	final int U_POSITION_TOO_LEFT = 0x00000800; // NO_UCD (use private)
	final int U_POSITION_TOO_LOW_RIGHT = (U_POSITION_TOO_LOW | U_POSITION_TOO_RIGHT); // NO_UCD
																						// (unused
																						// code)
	final int U_POSITION_TOO_LOW_LEFT = (U_POSITION_TOO_LOW | U_POSITION_TOO_LEFT); // NO_UCD
																					// (unused
																					// code)
	final int U_POSITION_TOO_TOP_RIGHT = (U_POSITION_TOO_TOP | U_POSITION_TOO_RIGHT); // NO_UCD
																						// (unused
																						// code)
	final int U_POSITION_TOO_TOP_LEFT = (U_POSITION_TOO_TOP | U_POSITION_TOO_LEFT); // NO_UCD
																					// (unused
																					// code)

	final int U_POSITION_OK = 0x00000000; // NO_UCD (unused code)

	final int U_DENSITY_CHECK_MASK = 0x000000E0; // NO_UCD (unused code)
	final int U_DENSITY_TOO_DARK = 0x00000020; // NO_UCD (unused code)
	final int U_DENSITY_TOO_LIGHT = 0x00000040; // NO_UCD (unused code)
	final int U_DENSITY_LITTLE_LIGHT = 0x00000060; // NO_UCD (unused code)
	final int U_DENSITY_AMBIGUOUS = 0x00000080; // NO_UCD (unused code)

	final int U_INSUFFICIENT_FP = -31; // NO_UCD (unused code)
	final int U_NOT_YET = -32; // NO_UCD (unused code)

	final int U_CLASS_A = 65; // NO_UCD (use private)
	final int U_CLASS_B = 66; // NO_UCD (use private)
	final int U_CLASS_C = 67; // NO_UCD (unused code)
	final int U_CLASS_D = 68; // NO_UCD (unused code)
	final int U_CLASS_E = 69; // NO_UCD (unused code)
	final int U_CLASS_R = 82; // NO_UCD (unused code)

	/** Called when the activity is first created. */
	private TextView theMessage;
	private ImageButton buttonConnect;
	private ImageButton buttonCapture;
	private ImageButton buttonEnroll;
	private ImageButton buttonVerify;
	private ImageButton buttonDisC;
	private EditText txtID;
	private int connectrtn;
	private int rtn;
	private ImageView myImage;

	private byte[] bMapArray = new byte[1078 + (640 * 480)];
	private byte[] minu_code1 = new byte[512];
	private byte[] minu_code2 = new byte[512];
	private String idtxt;

	private EventHandler m_eventHandler;
	private int SnapBusy = 0;

	private int counter = 0;
	public static Context mcontext;

	private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
	private UsbManager manager;
	private PendingIntent mPermissionIntent;
	private UsbDevice d;
	private UsbDeviceConnection conn;
	private UsbInterface usbIf;
	UsbEndpoint epIN; // NO_UCD (unused code)
	UsbEndpoint epOUT; // NO_UCD (unused code)
	UsbEndpoint ep2IN; // NO_UCD (unused code)

	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
				UsbDevice device = (UsbDevice) intent
						.getParcelableExtra(UsbManager.EXTRA_DEVICE);
				int pid, vid;
				pid = device.getProductId();
				vid = device.getVendorId();
				if (((pid == 0x8220) && (vid == 0x0bca))
						|| ((pid == 0x8220) && (vid == 0x0b39))
						|| ((pid == 0x8210) && (vid == 0x0b39))) {
					if (connectrtn == 0)
						tstlib.FP_DisconnectCaptureDriver();
					connectrtn = -1;
					theMessage.setText("fm220 removed");
					setImageButtonEnabled(mcontext, false, buttonConnect,
							R.drawable.connect);
					setImageButtonEnabled(mcontext, false, buttonCapture,
							R.drawable.snap);
					setImageButtonEnabled(mcontext, false, buttonEnroll,
							R.drawable.enroll);
					setImageButtonEnabled(mcontext, false, buttonVerify,
							R.drawable.verify);
					setImageButtonEnabled(mcontext, false, buttonDisC,
							R.drawable.disconnect);
				}
			}
			if (ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					UsbDevice device = (UsbDevice) intent
							.getParcelableExtra(UsbManager.EXTRA_DEVICE);

					if (intent.getBooleanExtra(
							UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						if (device != null) {
							// call method to set up device communication
							int pid, vid;
							pid = device.getProductId();
							vid = device.getVendorId();
							if (((pid == 0x8220) && (vid == 0x0bca))
									|| ((pid == 0x8220) && (vid == 0x0b39))
									|| ((pid == 0x8210) && (vid == 0x0b39))) {
								theMessage.setText("fm220 found");
								connectreader();
							}
						}
					} else {
						// Log.d(TAG, "permission denied for device " + device);
						theMessage.setText("fm220 found");
					}
				}
			}
		}
	};

	private void connectreader() {
		if (d == null)
			return;
		usbIf = d.getInterface(0);

		epIN = null;
		epOUT = null;
		ep2IN = null;
		epOUT = usbIf.getEndpoint(0);
		epIN = usbIf.getEndpoint(1);
		ep2IN = usbIf.getEndpoint(2);

		if (manager.hasPermission(d) == false) {
			return;
		}

		conn = manager.openDevice(d);

		if (conn.getFileDescriptor() == -1) {
			Log.d("FM220", "Fails to open DeviceConnection");
		} else {
			Log.d("FM220",
					"Opened DeviceConnection"
							+ Integer.toString(conn.getFileDescriptor()));
		}

		if (conn.releaseInterface(usbIf)) {
			Log.d("USB", "Released OK");
		} else {
			Log.d("USB", "Released fails");
		}

		if (conn.claimInterface(usbIf, true)) {
			Log.d("USB", "Claim OK");
		} else {
			Log.d("USB", "Claim fails");
		}
		theMessage.setText("fm220 Available"); // fileDesc" +
												// conn.getFileDescriptor());
		setImageButtonEnabled(mcontext, true, buttonConnect, R.drawable.connect);
		buttonConnect.performClick();

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
			UsbDevice device = (UsbDevice) intent
					.getParcelableExtra(UsbManager.EXTRA_DEVICE);
			// if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED,
			// false)) {
			if (device != null) {
				// call method to set up device communication & Check pid
				int pid, vid;
				pid = device.getProductId();
				vid = device.getVendorId();
				if (((pid == 0x8220) && (vid == 0x0bca))
						|| ((pid == 0x8220) && (vid == 0x0b39))
						|| ((pid == 0x8210) && (vid == 0x0b39))) {
					theMessage.setText("fm220 found");
					d = device;
					manager.requestPermission(d, mPermissionIntent);
					theMessage.setText("fm220 found");
					connectreader();
				}
			}
			// }
			else {
				// Log.d(TAG, "permission denied for device " + device);
				theMessage.setText("fm220 found");
			}
		}
	}

	/**
	 * Sets the image button to the given state and grays-out the icon.
	 * 
	 * @param enabled
	 *            The state of the button
	 * @param item
	 *            The button item to modify
	 * @param iconResId
	 *            The button's icon ID
	 */
	private static void setImageButtonEnabled(Context ctxt, boolean enabled,
			ImageButton item, int iconResId) {

		item.setEnabled(enabled);
		Drawable originalIcon = ctxt.getResources().getDrawable(iconResId);
		Drawable icon = enabled ? originalIcon
				: convertDrawableToGrayScale(originalIcon);
		item.setImageDrawable(icon);
	}

	/**
	 * Mutates and applies a filter that converts the given drawable to a Gray
	 * image. This method may be used to simulate the color of disable icons in
	 * Honeycomb's ActionBar.
	 * 
	 * @return a mutated version of the given drawable with a color filter
	 *         applied.
	 */
	private static Drawable convertDrawableToGrayScale(Drawable drawable) {
		if (drawable == null)
			return null;

		Drawable res = drawable.mutate();
		res.setColorFilter(Color.LTGRAY, Mode.LIGHTEN);
		return res;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mcontext = getApplicationContext();
		tstlib.SetFPLibraryPath(mcontext.getFilesDir().getParentFile()
				.getPath()
				+ "/lib/");
		tstlib.InitialSDK();
		theMessage = (TextView) findViewById(R.id.message);

		theMessage.setText("Access Computech Identification Demo");

		buttonConnect = (ImageButton) findViewById(R.id.connectB);
		buttonCapture = (ImageButton) findViewById(R.id.captureB);
		buttonEnroll = (ImageButton) findViewById(R.id.enrollB);
		buttonVerify = (ImageButton) findViewById(R.id.verifyB);
		buttonDisC = (ImageButton) findViewById(R.id.discB);
		myImage = (ImageView) findViewById(R.id.test_image);
		txtID = (EditText) findViewById(R.id.enrlID);

		setImageButtonEnabled(mcontext, false, buttonConnect,
				R.drawable.connect);
		setImageButtonEnabled(mcontext, false, buttonCapture, R.drawable.snap);
		setImageButtonEnabled(mcontext, false, buttonEnroll, R.drawable.enroll);
		setImageButtonEnabled(mcontext, false, buttonVerify, R.drawable.verify);
		setImageButtonEnabled(mcontext, false, buttonDisC,
				R.drawable.disconnect);

		Thread myThread = null;

		Runnable myRunnableThread = new CountDownRunner();
		myThread = new Thread(myRunnableThread);
		myThread.start();

		manager = (UsbManager) getSystemService(Context.USB_SERVICE);

		mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(
				ACTION_USB_PERMISSION), 0);
		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
		filter.addAction(ACTION_USB_PERMISSION);
		filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
		filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
		// filter.addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED);
		registerReceiver(mUsbReceiver, filter);

		// check for existing devices

		// PendingIntent mPermissionIntent;
		for (UsbDevice mdevice : manager.getDeviceList().values()) {

			int pid, vid;

			pid = mdevice.getProductId();
			vid = mdevice.getVendorId();

			if (((pid == 0x8220) && (vid == 0x0bca))
					|| ((pid == 0x8220) && (vid == 0x0b39))
					|| ((pid == 0x8210) && (vid == 0x0b39))) {
				theMessage.setText("fm220 found");
				d = mdevice;
				manager.requestPermission(d, mPermissionIntent);
				break;
			}

		}
		// ///ori connect here
		if (d == null) {
			theMessage.setText("fm220 not found. Pl connect sensor.");
		} else {
			setImageButtonEnabled(mcontext, true, buttonConnect,
					R.drawable.connect);
			buttonConnect.performClick();
		}
		
		// Connect
		buttonConnect.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {

				try {

					if (conn == null) {
						theMessage.setText("FM 220 not found");
						return;
					}
					if (conn.getFileDescriptor() == -1) {
						connectreader();
						connectrtn = tstlib.FP_ConnectCaptureDriver(conn
								.getFileDescriptor());
						if (connectrtn == 0) {
							theMessage.setText("Device ready");
							setImageButtonEnabled(mcontext, true,
									buttonCapture, R.drawable.snap);
							setImageButtonEnabled(mcontext, true, buttonEnroll,
									R.drawable.enroll);
							setImageButtonEnabled(mcontext, true, buttonVerify,
									R.drawable.verify);
							setImageButtonEnabled(mcontext, true, buttonDisC,
									R.drawable.disconnect);
						}
					} else {
						// theMessage.setText("try connect with file descripter"+
						// conn.getFileDescriptor());
						connectrtn = tstlib.FP_ConnectCaptureDriver(conn
								.getFileDescriptor());
						if (connectrtn == 0) {
							theMessage.setText("Device ready");
							setImageButtonEnabled(mcontext, true,
									buttonCapture, R.drawable.snap);
							setImageButtonEnabled(mcontext, true, buttonEnroll,
									R.drawable.enroll);
							setImageButtonEnabled(mcontext, true, buttonVerify,
									R.drawable.verify);
							setImageButtonEnabled(mcontext, true, buttonDisC,
									R.drawable.disconnect);
						}
					}
					return;
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		// Capture
		buttonCapture.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {

					if (connectrtn == 0) {
						theMessage.setText(" ");
						setImageButtonEnabled(mcontext, false, buttonConnect,
								R.drawable.connect);
						setImageButtonEnabled(mcontext, false, buttonCapture,
								R.drawable.snap);
						setImageButtonEnabled(mcontext, false, buttonEnroll,
								R.drawable.enroll);
						setImageButtonEnabled(mcontext, false, buttonVerify,
								R.drawable.verify);
						setImageButtonEnabled(mcontext, false, buttonDisC,
								R.drawable.disconnect);
						m_eventHandler = new EventHandler(Looper
								.getMainLooper());
						new Thread() {
							public void run() {
								super.run();
								counter = 0;
								System.gc();
								Message msg0 = new Message();
								msg0.what = PublicData.TEXTVIEW_CAPTURE_PLEASE_PRESS;
								m_eventHandler.sendMessage(msg0);
								while ((rtn = tstlib.FP_Capture()) != 0) {
									msg0 = new Message();
									msg0.what = PublicData.SHOW_PIC;
									m_eventHandler.sendMessage(msg0);
									if (counter > 15)
										break;
									counter++;
									if (rtn == -2 || connectrtn != 0) 
										break;
									try {
										Thread.sleep(50);
									} catch (InterruptedException e) {
									}
									while (SnapBusy == 1) {
										try {
											Thread.sleep(1);
										} catch (InterruptedException e) {
										}
									}
								}
								if (rtn == 0) {
									rtn = tstlib.FP_GetTemplate(minu_code1);
									int sz, un, ln;
									un = minu_code1[10];
									ln = minu_code1[11];
									if (ln < 0)
										ln = ln + 256;
									if (un < 0)
										ln = ln + 256;
									sz = (un * 256) + ln;
									byte[] ISOdata = new byte[sz];
									System.arraycopy(minu_code1, 0, ISOdata, 0,
											sz);
									// tstlib.FP_SaveImageBMP(Context.getFilesDir().getPath()+"/fp_image.bmp");
									msg0 = new Message();
									msg0.what = PublicData.TEXTVIEW_SUCCESS;
									m_eventHandler.sendMessage(msg0);
									msg0 = new Message();
									msg0.what = PublicData.SHOW_PIC;
									m_eventHandler.sendMessage(msg0);
								} else {
									// FP_LedOff();
									msg0 = new Message();
									msg0.what = PublicData.TEXTVIEW_FAILURE;
									m_eventHandler.sendMessage(msg0);
								}
								System.gc();
								return;
							}
						}.start();
					} else {
						theMessage
								.setText("FP_ConnectCaptureDriver() failed!!");
						theMessage.postInvalidate();
						tstlib.FP_DisconnectCaptureDriver();
						return;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		// Enroll
		buttonEnroll.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// led_off();
				if (txtID.getText().length() == 0 ){
					theMessage.setText("Pl enter enroll ID!!");
					return;
				}
				if (connectrtn == 0) {
					setImageButtonEnabled(mcontext, false, buttonConnect,
							R.drawable.connect);
					setImageButtonEnabled(mcontext, false, buttonCapture,
							R.drawable.snap);
					setImageButtonEnabled(mcontext, false, buttonEnroll,
							R.drawable.enroll);
					setImageButtonEnabled(mcontext, false, buttonVerify,
							R.drawable.verify);
					setImageButtonEnabled(mcontext, false, buttonDisC,
							R.drawable.disconnect);
					m_eventHandler = new EventHandler(Looper.getMainLooper());

					// let thread do main job
					new Thread() {
						public void run() {
							super.run();

							tstlib.FP_CreateEnrollHandle();
							Message msg = new Message();
							msg.what = PublicData.TEXTVIEW_ENROLL_PLEASE_PRESS;
							m_eventHandler.sendMessage(msg);
							for (int i = 0; i < 6; i++) {
								// theMessage.setText("Times: "+i);
								SystemClock.sleep(500);
								while ((rtn = tstlib.FP_Capture()) != 0) {
									msg = new Message();
									msg.what = PublicData.TEXTVIEW_PRESS_AGAIN;
									m_eventHandler.sendMessage(msg);
									msg = new Message();
									msg.what = PublicData.SHOW_PIC;
									m_eventHandler.sendMessage(msg);
									if (rtn == -2 || connectrtn != 0) 
									{
										msg = new Message();
										msg.what = PublicData.TEXTVIEW_FAILURE;
										m_eventHandler.sendMessage(msg);
										tstlib.FP_DestroyEnrollHandle();
										System.gc();
										return;
									}
									try {
										Thread.sleep(50);
									} catch (InterruptedException e) {
									}
									while (SnapBusy == 1) {
										try {
											Thread.sleep(1);
										} catch (InterruptedException e) {
										}
									}
								}
								rtn = tstlib.FP_GetTemplate(minu_code1);

								rtn = tstlib.FP_ISOminutiaEnroll(minu_code1,
										minu_code2);

								if (rtn == U_CLASS_A || rtn == U_CLASS_B) {
									tstlib.FP_SaveISOminutia(minu_code2,
											mcontext.getFilesDir().getPath()
													+ "/fp"+txtID.getText().toString().trim()+".dat");
									SystemClock.sleep(1000);
									msg = new Message();
									msg.what = PublicData.TEXTVIEW_SUCCESS;
									m_eventHandler.sendMessage(msg);
									break;
								} else if (i == 5) {
									msg = new Message();
									msg.what = PublicData.TEXTVIEW_FAILURE;
									m_eventHandler.sendMessage(msg);
									break;
								}
								while (true) {
									rtn = tstlib.FP_CheckBlank();
									msg = new Message();
									msg.what = PublicData.TEXTVIEW_REMOVE_FINGER;
									m_eventHandler.sendMessage(msg);
									if (rtn != -1)
										break;
								}
							}
							tstlib.FP_DestroyEnrollHandle();
							System.gc();
							return;
						}
					}.start();
				} else {
					theMessage.setText("FP_ConnectCaptureDriver() failed!!");
					tstlib.FP_DisconnectCaptureDriver();
					return;
				}

			}

		});
		
		//Verify
		buttonVerify.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if (connectrtn == 0) {
						setImageButtonEnabled(mcontext, false, buttonConnect,
								R.drawable.connect);
						setImageButtonEnabled(mcontext, false, buttonCapture,
								R.drawable.snap);
						setImageButtonEnabled(mcontext, false, buttonEnroll,
								R.drawable.enroll);
						setImageButtonEnabled(mcontext, false, buttonVerify,
								R.drawable.verify);
						setImageButtonEnabled(mcontext, false, buttonDisC,
								R.drawable.disconnect);
						m_eventHandler = new EventHandler(Looper
								.getMainLooper());

						new Thread() {
							public void run() {
								super.run();
								FilenameFilter filter = new FilenameFilter() {
									public boolean accept(File directory, String fileName) {
										return fileName.endsWith(".dat") && fileName.startsWith("fp");
									}
								};
								File[] fplst;
								fplst = mcontext.getFilesDir().listFiles(filter);



								if (fplst.length>0) {
									Message msg = new Message();
//									msg.what = PublicData.TEXTVIEW_FILE_EXIST;
//									m_eventHandler.sendMessage(msg);
									if (connectrtn == 0) {
									} else {
										tstlib.FP_DisconnectCaptureDriver();
										return;
									}
									msg = new Message();
									msg.what = PublicData.TEXTVIEW_VERIFY_PLEASE_PRESS;
									m_eventHandler.sendMessage(msg);

									while ((rtn = tstlib.FP_Capture()) != 0) {
										msg = new Message();
										msg.what = PublicData.SHOW_PIC;
										m_eventHandler.sendMessage(msg);
										if (rtn == -2 || connectrtn != 0) 
										{
											msg = new Message();
											msg.what = PublicData.TEXTVIEW_FAILURE;
											m_eventHandler.sendMessage(msg);
											return;
										}
										try {
											Thread.sleep(50);
										} catch (InterruptedException e) {
										}
										while (SnapBusy == 1) {
											try {
												Thread.sleep(1);
											} catch (InterruptedException e) {
											}
										}
									}
									// tstlib.FP_SaveImageBMP(Context.getFilesDir().getPath()+"/fp_image.bmp");

									rtn = tstlib.FP_GetTemplate(minu_code1);
									if (rtn < 0) {
										msg = new Message();
										msg.what = PublicData.TEXTVIEW_FAILURE;
										m_eventHandler.sendMessage(msg);
										System.gc();
										return;
									}
									idtxt = "";
									for (int fctr=0; fctr<fplst.length; fctr++) {
										String fpnm = fplst[fctr].getName();
										if(fpnm.lastIndexOf("fpcode",0)>=0) continue;
										rtn = tstlib.FP_LoadISOminutia(minu_code2,
												fplst[fctr].getAbsolutePath());
										if (rtn == 0) {
											rtn = tstlib.FP_ISOminutiaMatch360Ex(
													minu_code1, minu_code2);
											if (rtn >= 0) {
												if (fpnm.lastIndexOf("fp",0)>=0) {
													fpnm = fpnm.substring(fpnm.lastIndexOf("fp", 0)+2);
													fpnm = fpnm.substring(0,fpnm.length()-4);
													idtxt = fpnm;
												}
												msg = new Message();
												msg.what = PublicData.TEXTVIEW_SCORE;
												m_eventHandler.sendMessage(msg);
												System.gc();
												return;
											}
										}
									}
									msg = new Message();
									msg.what = PublicData.TEXTVIEW_FAILURE;
									m_eventHandler.sendMessage(msg);
									System.gc();
								} else {
									Message msg4 = new Message();
									msg4.what = PublicData.TEXTVIEW_FILE_NOT_EXIST;
									m_eventHandler.sendMessage(msg4);
									return;
								}
							}
						}.start();
					} else {
						theMessage
								.setText("FP_ConnectCaptureDriver() failed!!");
						theMessage.postInvalidate();
						tstlib.FP_DisconnectCaptureDriver();
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		buttonDisC.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				conn.close();
				tstlib.FP_DisconnectCaptureDriver();
				theMessage.setText("Disconnect Succeeded!!");
				theMessage.postInvalidate();
				setImageButtonEnabled(mcontext, true, buttonConnect,
						R.drawable.connect);
				setImageButtonEnabled(mcontext, false, buttonCapture,
						R.drawable.snap);
				setImageButtonEnabled(mcontext, false, buttonEnroll,
						R.drawable.enroll);
				setImageButtonEnabled(mcontext, false, buttonVerify,
						R.drawable.verify);
				setImageButtonEnabled(mcontext, false, buttonDisC,
						R.drawable.disconnect);
				return;
			}
		});

	}
	
	@Override
	public void onBackPressed() {
		if(isTaskRoot()) {
	    new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setCancelable(false)
	        .setTitle("Access UID")
	        .setMessage("Are you sure you want to exit?")
	        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
	    {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	        	if (conn != null && connectrtn == 0){
		        	conn.close();
					tstlib.FP_DisconnectCaptureDriver();
	        	}
	        	AccessFM2xx.super.onBackPressed();   
	        }

	    })
	    .setNegativeButton("No", new DialogInterface.OnClickListener()
	    {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	        	dialog.cancel();   
	        }

	    })
	    .show();
		} else {
			super.onBackPressed();
		}
	}
	
	
	private void ShowTime() {
		runOnUiThread(new Runnable() {
			public void run() {
				try {

					TextView txtCurrentTime = (TextView) findViewById(R.id.myTime);
					Date dt = new Date();
					String curTime = DateFormat.format("EEE dd MMM HH:mm:ss",
							dt).toString(); // hours + ":" + minutes + ":" +
											// seconds;
					txtCurrentTime.setText(curTime);
				} catch (Exception e) {
				}
			}
		});
	}

	private class CountDownRunner implements Runnable {
		// @Override
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					ShowTime();
					Thread.sleep(1000); // Pause of 1 Second
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				} catch (Exception e) {
				}
			}
		}
	}

	private class showPic extends AsyncTask<String, Void, String> {
		// private ImageView image;
		private Bitmap bMap = null;

		@Override
		protected String doInBackground(String... path) {
			tryGetStream();
			return null;
		}

		protected void onPostExecute(String a) { // NO_UCD (unused code)
			if (bMap != null) {
				if (theMessage.getText() != "Failure.") {
				myImage.setImageBitmap(bMap);
				}
				bMap = null;
			} else {

			}
			System.gc();
			publishProgress();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
			myImage.postInvalidate();
		}

		private void tryGetStream() {
			try {
				SnapBusy = 1;
				tstlib.FP_GetImageBuffer(bMapArray);
				bMap = BitmapFactory.decodeByteArray(bMapArray, 0,
						bMapArray.length);
				SnapBusy = 0;
			} catch (Exception e) {
				SnapBusy = 0;
			}
		}

	}

	private class EventHandler extends Handler {
		private EventHandler(Looper looper) {
			super(looper);
		}
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case PublicData.TEXTVIEW_SUCCESS:
				setImageButtonEnabled(mcontext, true, buttonConnect,
						R.drawable.connect);
				setImageButtonEnabled(mcontext, true, buttonCapture,
						R.drawable.snap);
				setImageButtonEnabled(mcontext, true, buttonEnroll,
						R.drawable.enroll);
				setImageButtonEnabled(mcontext, true, buttonVerify,
						R.drawable.verify);
				setImageButtonEnabled(mcontext, true, buttonDisC,
						R.drawable.disconnect);
				theMessage.setText("Success.");
				theMessage.postInvalidate();
				break;
			case PublicData.TEXTVIEW_FAILURE:
				if (connectrtn == 0) {
					setImageButtonEnabled(mcontext, true, buttonConnect,
							R.drawable.connect);
					setImageButtonEnabled(mcontext, true, buttonCapture,
							R.drawable.snap);
					setImageButtonEnabled(mcontext, true, buttonEnroll,
							R.drawable.enroll);
					setImageButtonEnabled(mcontext, true, buttonVerify,
							R.drawable.verify);
					setImageButtonEnabled(mcontext, true, buttonDisC,
							R.drawable.disconnect);
					myImage.setImageResource(R.drawable.fingerprint);
					theMessage.setText("Failure.");
					theMessage.postInvalidate();
				} else {
					theMessage.setText("fm220 removed");
				}
				break;
			case PublicData.TEXTVIEW_CAPTURE_PLEASE_PRESS:
				theMessage.setText("Capture: Press your finger");
				theMessage.postInvalidate();
				break;
			case PublicData.TEXTVIEW_ENROLL_PLEASE_PRESS:
				theMessage.setText("Enroll: Press your finger");
				theMessage.postInvalidate();
				break;
			case PublicData.TEXTVIEW_VERIFY_PLEASE_PRESS:
				theMessage.setText("Verify: Press your finger");
				theMessage.postInvalidate();
				break;
			case PublicData.TEXTVIEW_SCORE:
				theMessage.setText("matching score=" + (int) tstlib.Score());
				theMessage.postInvalidate();
				txtID.setText(idtxt);
				txtID.postInvalidate();
				setImageButtonEnabled(mcontext, true, buttonConnect,
						R.drawable.connect);
				setImageButtonEnabled(mcontext, true, buttonCapture,
						R.drawable.snap);
				setImageButtonEnabled(mcontext, true, buttonEnroll,
						R.drawable.enroll);
				setImageButtonEnabled(mcontext, true, buttonVerify,
						R.drawable.verify);
				setImageButtonEnabled(mcontext, true, buttonDisC,
						R.drawable.disconnect);
				break;
			case PublicData.TEXTVIEW_FILE_EXIST:
				theMessage.setText("Verify: File exist");
				theMessage.postInvalidate();
				break;
			case PublicData.TEXTVIEW_FILE_NOT_EXIST:
				theMessage.setText("File not exist, please enroll first");
				theMessage.postInvalidate();
				setImageButtonEnabled(mcontext, true, buttonConnect,
						R.drawable.connect);
				setImageButtonEnabled(mcontext, true, buttonCapture,
						R.drawable.snap);
				setImageButtonEnabled(mcontext, true, buttonEnroll,
						R.drawable.enroll);
				setImageButtonEnabled(mcontext, true, buttonVerify,
						R.drawable.verify);
				setImageButtonEnabled(mcontext, true, buttonDisC,
						R.drawable.disconnect);
				break;
			case PublicData.TEXTVIEW_REMOVE_FINGER:
				theMessage.setText("Please remove your finger");
				theMessage.postInvalidate();
				new showPic().execute("");
				break;
			case PublicData.TEXTVIEW_PRESS_AGAIN:
				theMessage.setText("Please press your finger again");
				theMessage.postInvalidate();
				new showPic().execute("");
				break;
			case PublicData.SHOW_PIC:
				new showPic().execute("");
				break;
			}
			super.handleMessage(msg);
		}
	}
}
// keyFileStream =
// AccessFM2xx.mcontext.getResources().getAssets().open(keyStoreFile);