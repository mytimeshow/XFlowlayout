package com.alan.xflowlayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by alan on  2019/4/9 12:56
 */
@SuppressLint("NewApi")
public class XFlowLayout extends Flowlayout {
    public static final int HORIZONTAL = 10010;
    public static final int VERTICAL = 10011;

    List<String> textLsit = new ArrayList<>();

    List<TextView> textViews = new ArrayList<>();

    List<TextView> selectedTextViews = new ArrayList<>();

    private ItemOnclickListener onItemClickListener;

    private ItemLongpressListener onItemLongPressListener;

    private boolean enableMaxSelect;

    private boolean isRandomTextColor;


    public XFlowLayout(Context context) {
       this(context,null);
    }

    public XFlowLayout(Context context, AttributeSet attrs) {
       super(context,attrs);
       setWillNotDraw(false);
    }



    public XFlowLayout setTexts(String[] texts) {
        ArrayList<String> list = new ArrayList<>();
        for (String text : texts) {
            list.add(text);
        }
        setTexts(list);
        return this;
    }

    public XFlowLayout setTexts(ArrayList<String> texts) {
        textLsit.clear();
        textViews.clear();
        if (selectedTexts == null) selectedTexts = new ArrayList<>();
        if(flag!=3)selectedTexts.clear();

        removeAllViews();

        for (final String text : texts) {
            textLsit.add(text);

            final TextView textView;

            if (isSingleSelect || isMoreSelect) textView = new TextView(getContext());
            else
                textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.textview_layout, this, false);
            textViews.add(textView);

            textView.setText(text);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(defaultTextColor);
            textView.setPadding(itemContentPadding + 5, itemContentPadding, itemContentPadding + 5, itemContentPadding + 7);

            if (drawableId > 0) {
                textView.setBackgroundResource(drawableId);
            } else {
                if(!isAllAttrsDefault)
                textView.setBackground(defaultDrawable());
                ;
            }

            if (isRandomTextColor) textView.setTextColor(Color.parseColor(getRandColor()));

            textView.setTextSize(textSize);
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isSingleSelect) setSingleSelected(text, textView);
                    if (isMoreSelect) moreSelect(text, textView);
                    if (onItemClickListener != null) onItemClickListener.onClick(text, textView);
                }
            });
            textView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongPressListener != null)
                        onItemLongPressListener.onLongClick(text, textView);

                    return true;
                }
            });



            addView(textView);
        }
        initSelected();
        return this;
    }

    private void initSelected() {
        if(flag<0)return;
        if(flag==1){
        selectedFlag1();
        }else if(flag==2){
            selectedFlag2();
        }else if(flag==3){
            selectedFlag3();
        }
    }

    private void selectedFlag1(){
        if(index<0 || index>textViews.size()){
            flag=-1;
            throw new IndexOutOfBoundsException("index out of bounds ,please check the index");
        }
        String text=textLsit.get(index);
        TextView textView=textViews.get(index);
        moreSelect(text,textView);
    }

    private void selectedFlag2(){
        if(index<0 || index>textViews.size() || index>textViews.size()-1 || index>end){
            flag=-1;
            throw new IndexOutOfBoundsException("index out of bounds ,please check the index");
        }
        if(selectedTexts==null)selectedTexts=new ArrayList<>();
        if(end >textViews.size()-1) end =textViews.size()-1;
        for (int i =index; i <= end; i++) {
            selectedTexts.add(textLsit.get(i));
            selectedTextViews.add(textViews.get(i));
        }
        for (TextView selectedTextView : selectedTextViews) {
            selectedTextView.setBackground(selectedDrawable());
            selectedTextView.setTextColor(slectedTextColor);
        }
    }

    private void selectedFlag3(){
        for (String selectedText : selectedTexts) {
            for (TextView textView : textViews) {
                if(selectedText.equals(textView.getText().toString().trim())){
                    selectedTextViews.add(textView);
                    break;
                }
            }
        }
        for (TextView selectedTextView : selectedTextViews) {
            selectedTextView.setBackground(selectedDrawable());
            selectedTextView.setTextColor(slectedTextColor);
        }
    }

    private void moreSelect(String text, TextView textView) {
        if (selectedTexts == null) selectedTexts = new ArrayList<>();
        if (!selectedTextViews.contains(textView)) {
            if (enableMaxSelect && selectedTextViews.size() >= maxSelect) return;
            selectedTextViews.add(textView);
            selectedTexts.add(text);
        } else {
            selectedTextViews.remove(textView);
            selectedTexts.remove(text);
            textView.setBackground(defaultDrawable());
            textView.setTextColor(defaultTextColor);
            return;
        }
        for (TextView selectedTextView : selectedTextViews) {
            selectedTextView.setBackground(selectedDrawable());
            selectedTextView.setTextColor(slectedTextColor);
        }
    }

    private XFlowLayout setSingleSelected(String text, TextView textView) {
        if(selectedTexts!=null)selectedTexts.clear();
        if(selectedTextViews!=null)selectedTextViews.clear();
        for (TextView view : textViews) {
            view.setTextColor(defaultTextColor);
            view.setBackground(defaultDrawable());
        }
        textView.setTextColor(slectedTextColor);
        textView.setBackground(selectedDrawable());
        singleTeSelectedText = text;
        return this;
    }

    /*
    * set which selected at the beginging
    *
    * */
    public XFlowLayout setSelected(int index,int length){
        if(index>=0 && length>1){
            this.index=index;
            this.end =length;
            flag=2;
        }else {
            flag=-1;
        throw new IllegalArgumentException(" Index must be greater than or equal to zero and end is greater than 1 ");
        }



        return this;
    }

    /*
     * set which selected at the beginging
     * */
    public XFlowLayout setSelected(int index){
        flag=1;
        this.index=index;
        return this;
    }

    /*
     * set which selected at the beginging
     * texts     those is being selected
     * */
    public XFlowLayout setSelected(String[] texts){
        if(texts!=null && texts.length>0){
            if(selectedTexts==null)selectedTexts=new ArrayList<>();
            for (String text : texts) {
                selectedTexts.add(text);
            }
            flag=3;
        }else {
            flag=-1;
            throw new IllegalArgumentException("texts must not null or texts.end must than 0");
        }
        return this;
    }

    /*
    * custom drawable
    * */
    public XFlowLayout setItemBackGroudDrawable( int drawableId) {
        if (drawableId > 0) this.drawableId = drawableId;

        return this;
    }


    /*
    * item background color of default
    * */
    public XFlowLayout setItemDefaultBackGroundColor(int color) {
        if(isAllAttrsDefault)return this;
        defaultBackGroundColor = color;
        return this;
    }


    /*
    * item background color while selected
    * */
    public XFlowLayout setItemSelectedBackGroundColor(int color) {
        if(isAllAttrsDefault)return this;
        selectedbackGroundColor = color;
        return this;
    }


    /*
    * set whether single select
    * */
    public XFlowLayout setIsSingleSlect(boolean isSingleSelect) {
        if(isAllAttrsDefault || isMoreSelect)return this;
        this.isSingleSelect = isSingleSelect;
        if (isSingleSelect) isMoreSelect = false;
        return this;
    }

    /*
    * text size
    * */
    public XFlowLayout setTextSize(float spValue) {
        if(isAllAttrsDefault)return this;
        textSize = spValue;
        return this;
    }

   /*
   * DefaultTextColor
   * */
    public XFlowLayout setDefaultTextColor(int color) {
        if(isAllAttrsDefault)return this;
        defaultTextColor = color;
        return this;
    }

    /*
    * TextColor when selected
    * */
    public XFlowLayout setSelectTextColor(int selectedColor) {
        if(isAllAttrsDefault)return this;
        slectedTextColor = selectedColor;

        return this;
    }

    /*
    * RadiuSize
    * */
    public XFlowLayout setRadiuSize(float radiuSize) {
        if(isAllAttrsDefault)return this;
        radius = radiuSize;
        return this;
    }

    /*
    * DefaultStokeColor
    * */
    public XFlowLayout setDefaultStokeColor(int defaultColor) {
        if(isAllAttrsDefault)return this;
        defaultStokeColor = defaultColor;
        return this;
    }

    /*
    * Border color when selected
    * */
    public XFlowLayout setSelectedStokeColor(int selectedColor) {
        if(isAllAttrsDefault)return this;
        selectedStokeColor = selectedColor;
        return this;
    }

   /*
   * Border size
   * */
    public XFlowLayout setStokeSize(int stokeSize) {
        if(isAllAttrsDefault)return this;
        stokeWidth = stokeSize;
        return this;
    }

    /*
    * Item horizontal spacing
    * */
    public XFlowLayout setItemInternalHorizontal(int pxValue) {
        if(isAllAttrsDefault)return this;
        itemInternal_horizontal = pxValue;
        return this;
    }

    /*
     * Item vertical spacing
     * */
    public XFlowLayout setItemInternalVertical(int pxValue) {
        if(isAllAttrsDefault)return this;
        itemInternal_vertical = pxValue;
        return this;
    }

    /*
    * texteView index
    * */
    public void delete(int index){
        if(index <0 | index>getChildCount()-1)throw new IndexOutOfBoundsException("index out of bounds ,please check the index");
        removeViewAt(index);
        textViews.remove(index);
        TextView child= (TextView) getChildAt(index);
        selectedTextViews.remove(child);
    }

    /*
     * the View will be remove
     * */
    public void delete(TextView textView){
        if(textView==null)return;
        selectedTextViews.remove(textView);
        textViews.remove(textView);
        removeView(textView);
    }

    /*
    * Orirntation
    * */
    public XFlowLayout setOrirntation(int ortentation) {
        if(isAllAttrsDefault)return this;
        this.orientation = ortentation;
        return this;
    }


    /*
    * Selected text when returning to single selection
    * */
    public String getSingleSelectedText() {

        return singleTeSelectedText;
    }


    /*
    * Selected text when multiple selection
    * */
    public ArrayList<String> getMoreSelectedText() {

        return selectedTexts;
    }

    /*
    * all setting before is invalid,and all setting will be the default
    * */
    public XFlowLayout setAllDefault(boolean isAllAttrsDefault ){
        this.isAllAttrsDefault=isAllAttrsDefault;
        if(isAllAttrsDefault){
            reSet();
        }
        return this;
    }

    private void reSet() {
        orientation =HORIZONTAL;
        itemInternal_horizontal = 20;
        itemInternal_vertical =  20;
        radius = getResources().getDimension(R.dimen.dp_4);
        isSingleSelect =  false;
        isMoreSelect = false;
        maxSelect =  3;
        textSize =  15F;
        itemContentPadding = (int)this.getResources().getDimension(R.dimen.dp_6);
        defaultTextColor =  Color.parseColor("#CCCCCC");
        slectedTextColor =  Color.parseColor("#CCCCCC");
        defaultBackGroundColor = Color.WHITE;
        selectedbackGroundColor = Color.WHITE;
        stokeWidth =  1;
        drawableId=0;
        defaultStokeColor = Color.parseColor("#CCCCCC");
        selectedStokeColor =  Color.RED;
    }

    /*
    * Set whether the color of each text is randomly selected
    * */
    public XFlowLayout setItemRandowColor(boolean isRandomColor) {
        if(isAllAttrsDefault)return this;
        isSingleSelect = false;
        isMoreSelect = false;
        isRandomTextColor = isRandomColor;
        return this;
    }

   /*
   * Inner spacing
   * */
    public XFlowLayout setItemContentPadding(int pxValue) {
        if(isAllAttrsDefault)return this;
        itemContentPadding = pxValue;
        return this;
    }

    /*
    * random color
    * */
    private String getRandColor() {
        String r, g, b;
        Random random = new Random();
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();

        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;

        return "#" + r + g + b;

    }

    /*
    * Set whether to choose multiple
    * */
    public XFlowLayout setIsMoreSelected(boolean isMoreSlect) {
        if(isAllAttrsDefault || isSingleSelect)return this;

        isMoreSelect = isMoreSlect;
        if (isMoreSelect) {
            isSingleSelect = false;
        }
        return this;
    }



    public XFlowLayout setOnItemClickListener(ItemOnclickListener itemOnclickListener) {
        onItemClickListener = itemOnclickListener;
        return this;
    }

    public XFlowLayout setOnItemLongPressListener(ItemLongpressListener itemLongPressListener) {
        onItemLongPressListener = itemLongPressListener;
        return this;
    }

    public XFlowLayout setMaxSelect(int maxSize) {
        enableMaxSelect = true;
        maxSelect = maxSize;
        return this;
    }


}
