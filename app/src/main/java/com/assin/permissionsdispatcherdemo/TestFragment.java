package com.assin.permissionsdispatcherdemo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Assassin on 2017/6/27.
 */

@RuntimePermissions
public class TestFragment extends Fragment {

    private Button callPhoneBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test,container,false);
        callPhoneBtn = (Button) view.findViewById(R.id.btn_fragment_camera);
        callPhoneBtn.setOnClickListener(callPhoneListener);
        return view;
    }

    private void showToast(String s){
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }


    View.OnClickListener callPhoneListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TestFragmentPermissionsDispatcher.callPhoneWithCheck(TestFragment.this,"13896040018");
        }
    };

    @NeedsPermission(Manifest.permission.CALL_PHONE)
    void callPhone(String phoneNum){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+phoneNum));
        startActivity(intent);
    }

    @OnShowRationale(Manifest.permission.CALL_PHONE)
    void showRationaleForRecord(final PermissionRequest request){
        new AlertDialog.Builder(getContext())
                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("不给", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage("此功能需要拨打电话的权限，应用将要申请权限")
                .show();
    }

    @OnPermissionDenied(Manifest.permission.CALL_PHONE)
    void showRecordDenied(){
        showToast("被拒绝使用打电话的权限");
    }

    @OnNeverAskAgain(Manifest.permission.CALL_PHONE)
    void onRecordNeverAskAgain() {
        new AlertDialog.Builder(getContext())
                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: 2016/11/10 打开系统设置权限
                        dialog.cancel();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage("您已经禁止了拨打电话的权限,是否现在去开启")
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        TestFragmentPermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);
    }
}
