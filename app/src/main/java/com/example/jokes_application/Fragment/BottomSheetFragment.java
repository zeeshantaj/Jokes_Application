package com.example.jokes_application.Fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.jokes_application.Adapter.PostRecyclerAdapter;
import com.example.jokes_application.Interfaces.BottomSheetListener;
import com.example.jokes_application.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetFragment extends BottomSheetDialogFragment  {

    public BottomSheetFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  =  inflater.inflate(R.layout.bottom_sheet_fragment, container, false);

        Button saveBtn = view.findViewById(R.id.savePostBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "save post", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        return view;
    }

}
