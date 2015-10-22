package com.awesome.aarslanaliyev.awesometodoapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;


public class EditItemFragment extends DialogFragment {

    public interface OnItemEditedListener {
        void onSave(String itemValue, Integer itemPosition);
    }

    private OnItemEditedListener mListener;

    static final String EDIT_ITEM_KEY = "edit_item";
    static final String POSITION_KEY = "item_position";

    static EditItemFragment instance(String item, Integer position) {
        EditItemFragment editItemFragment = new EditItemFragment();
        Bundle args = new Bundle();
        args.putString(EDIT_ITEM_KEY, item);
        args.putInt(POSITION_KEY, position);
        editItemFragment.setArguments(args);
        return editItemFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final String item = getArguments().getString(EDIT_ITEM_KEY);
        final Integer position = getArguments().getInt(POSITION_KEY);
        String itemValue = "";
        if (item != null) {
            String[] split = item.split("-");
            itemValue = split[0].trim();
        }
        final View dialogView = inflater.inflate(R.layout.edit_modal, null);
        final EditText modalEditText = (EditText)dialogView.findViewById(R.id.edit_item);
        modalEditText.setText(itemValue);

        builder.setView(dialogView)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        notifyListeners(modalEditText.getText().toString(), position);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditItemFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    public void notifyListeners(String itemValue, Integer position) {
        this.mListener.onSave(itemValue, position);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnItemEditedListener)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnItemEditedListener");
        }
    }
}
