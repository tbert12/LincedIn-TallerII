package com.fiuba.tallerii.lincedin.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.model.comparators.SkillComparator;
import com.fiuba.tallerii.lincedin.model.user.UserSkill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SkillsSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private final Context context;
    private List<UserSkill> dataset = new ArrayList<>();

    public SkillsSpinnerAdapter(Context context) {
        this.context = context;
    }

    public SkillsSpinnerAdapter(Context context, List<UserSkill> dataset) {
        this.context = context;
        setDataset(dataset);
    }

    public void setDataset(List<UserSkill> dataset) {
        this.dataset = dataset;
        Collections.sort(this.dataset, new SkillComparator());
    }

    @Override
    public int getCount() {
        return dataset.size();
    }

    @Override
    public UserSkill getItem(int position) {
        return dataset.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.user_skill_spinner_row, parent, false);
        }

        UserSkill currentSkill = getItem(position);
        if (currentSkill != null) {
            ((TextView) convertView.findViewById(R.id.user_skill_spinner_row_name_textview)).setText(currentSkill.name);
            ((TextView) convertView.findViewById(R.id.user_skill_spinner_row_category_textview)).setText(currentSkill.category);
        }

        return convertView;
    }
}
