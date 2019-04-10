# XFlowlayout
在开始之前，先来看下效果图吧

排版好像有问题，觉得难看的可以去博客上看    https://blog.csdn.net/qq_mytime/article/details/89158539


![image](https://github.com/mytimeshow/XFlowlayout/blob/master/images/dd.gif)
![image](https://github.com/mytimeshow/XFlowlayout/blob/master/images/qq.gif)
调用

首先导入下依赖

implementation’com.mytimeshow:xflowlayout:1.1.1’

  flowlayout
                .setRadiuSize(radiu.toFloat())
                .setItemSelectedBackGroundColor(s_bc_c)
                .setItemDefaultBackGroundColor(dd_bc_c)
                .setDefaultStokeColor(d_sto_c)
                .setSelectedStokeColor(s_sto_c)
                .setTextSize(tests.toFloat())
                .setDefaultTextColor(d_text_c)
                .setSelectTextColor(s_text_c)
                .setStokeSize(d_sto_s)
                .setItemRandowColor(r)
                .setIsSingleSlect(s)
                .setMaxSelect(max_)
                .setIsMoreSelected(m)
                .setItemInternalHorizontal(space_h)
                .setOrirntation(if(l)Flowlayout.HORIZONTAL else Flowlayout.VERTICAL)
                .setItemInternalVertical(space_v)
                .setItemContentPadding(contentp)
                .setAllDefault(d)
                .setOnItemClickListener(object: ItemOnclickListener {
                    override fun onClick(text: String, textView: TextView) {
                        flowlayout.getMoreSelectedText()?.forEach { t: String? ->Log.e("flowlayout",t)  }

                    }
                })
                 .setOnItemLongPressListener(object: ItemLongpressListener {
                    override fun onLongClick(var1: String, var2: TextView) {

                    }
                })
                .setTexts(texts)




删除

 flowlayout.delete(3);
 flowlayout.delete(textView);

设置选中

 flowlayout.setSelected(new String[]{"我们","我爱你","哈哈哈"});
                    flowlayout.setSelected(3);
                    flowlayout.setSelected(3,6);         index,end
       
 **flowlayout.setSelected(index,end);    index->开始删除的位置，end->结束删除的位置**
1
2
3
4
5
多选时，获取已选中的text列表f

 flowlayout.getMoreSelectedText();
1
获取单选时，选中的text

 flowlayout.getSingleSelectedText();
 
 
1
注意事项 单选和多选是具有互斥作用的，所以不要设置了多选，又设置了单选






在写这个控件之前，其实我是有这方面的需求的，首先是项目中有用到历史搜索的功能，因此，作为讲究“效率”的程序员，想到的就是马上百度一波，看看有没有合适的，拿来就用。不过很可惜，我并没有找到合适项目ui要求的，因为我的项目中，不仅仅是普通的自动换行的流式布局控件，还要可以自行设置textview的圆角值，默认与选中时的文字颜色或背景颜色，还最特别的一点是，一般的flowlayout只支持横向的布局，我的项目中有些地方还要用到竖向的布局，所以，我就打算撸一个flowlayoutba吧，为了区别于其他的flowlayout，我特意起在前面加了一个X，叫XFlowlayout

当然啦，作为追求美观的程序员，肯定是支持在布局文件中配置这些个参数的，不喜欢在代码中设置的可以在布局文件中去配置一些参数，下面列出自定义属性吧

 <resources>

    <declare-styleable name="FlowLayout">

        <attr name="orientation" >
            <enum name="vertical" value="1"/>
            <enum name="horizontal" value="0"/>
        </attr>

        <attr name="itemInternal_horizontal" format="integer"/>

        <attr name="itemInternal_vertical" format="integer"/>

        <attr name="item_radius" format="float"/>

        <attr name="singgle_select" format="boolean"/>

        <attr name="moreselect_select" format="boolean"/>

        <attr name="max_select" format="integer"/>

        <attr name="text_size" format="integer"/>

        <attr name="default_textColor" format="color"/>

        <attr name="item_content_internal" format="integer"/>

        <attr name="selected_textColor" format="color"/>

        <attr name="item_default_backGroundDrawable" format="color"/>

        <attr name="item_select_backGroundDrawable" format="color"/>

        <attr name="stoke_width" format="integer"/>

        <attr name="default_stoke_color" format="color"/>

        <attr name="selected_stoke_color" format="color"/>
    </declare-styleable>



</resources>

大部分都是望文生义的属性，我就不专门写注释了
下面就简单来讲解下代码吧

  public static final int  HORIZONTAL = 10010;
    public static final int VERTICAL = 10011;
    protected int itemInternal_horizontal;  									//水平方向的间隙
    protected int itemInternal_vertical;											//垂直方向的间隙
    protected int itemContentPadding;											//内间距  相当于padding
    protected int orientation;															//布局方向
    protected int defaultBackGroundColor;						//默认背景颜色
    protected int selectedbackGroundColor;							//多选 或单选时，选中的时候的背景颜色
    protected float radius;														//圆角值
    protected int slectedTextColor;											//多选 或单选时，被选中的文字颜色
    protected int defaultTextColor;										//默认文字颜色
    protected String singleTeSelectedText;									//单选时，被选中的text
    protected ArrayList<String> selectedTexts;									//多选时，被选中的text列表
    protected boolean isSingleSelect;										//是否是单选
    protected boolean isMoreSelect;										//是都多选
    protected float textSize;													//文字大小
    protected int defaultStokeColor;												//默认的边框颜色
    protected int selectedStokeColor;											//被选中时的边框颜色
    protected int maxSelect;													//多选时，最多能选中多少个
    protected int stokeWidth;														//边框大小
    protected int drawableId;														//传入自己的drawable id  比如      R.drawable.bg-round
    protected boolean isAllAttrsDefault;								//是否全部属性都设为默认配置，当isAllAttrsDefault为true时，再去设置其他属性是不起作用的了
  

    public Flowlayout(Context context) {
       this(context,null);
    }

    public Flowlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
       orientation = typeArray.getInt(R.styleable.FlowLayout_orientation, HORIZONTAL);
       itemInternal_horizontal = typeArray.getInt(R.styleable.FlowLayout_itemInternal_horizontal, 20);
       itemInternal_vertical = typeArray.getInt(R.styleable.FlowLayout_itemInternal_vertical, 20);
       radius = typeArray.getFloat(R.styleable.FlowLayout_item_radius,getResources().getDimension(R.dimen.dp_4));
       isSingleSelect = typeArray.getBoolean(R.styleable.FlowLayout_singgle_select, false);
       isMoreSelect = typeArray.getBoolean(R.styleable.FlowLayout_moreselect_select, false);
       maxSelect = typeArray.getInt(R.styleable.FlowLayout_max_select, 3);
       textSize = typeArray.getFloat(R.styleable.FlowLayout_text_size, 15.0F);
       itemContentPadding = typeArray.getInt(R.styleable.FlowLayout_item_content_internal, (int)this.getResources().getDimension(R.dimen.dp_6));
       defaultTextColor = typeArray.getColor(R.styleable.FlowLayout_default_textColor, Color.parseColor("#CCCCCC"));
       slectedTextColor = typeArray.getColor(R.styleable.FlowLayout_selected_textColor, Color.parseColor("#CCCCCC"));
       defaultBackGroundColor = typeArray.getColor(R.styleable.FlowLayout_item_default_backGroundDrawable, Color.WHITE);
       selectedbackGroundColor = typeArray.getColor(R.styleable.FlowLayout_item_select_backGroundDrawable, Color.WHITE);
       stokeWidth = typeArray.getInt(R.styleable.FlowLayout_stoke_width, 1);
       defaultStokeColor = typeArray.getColor(R.styleable.FlowLayout_default_stoke_color, Color.parseColor("#CCCCCC"));
       selectedStokeColor = typeArray.getColor(R.styleable.FlowLayout_selected_stoke_color, Color.RED);
       typeArray.recycle();
       layoutManager=new LayoutManage(this);
    }


    public  GradientDrawable defaultDrawable() {
        Drawable var10000 =getContext().getDrawable(R.drawable.bg_default);
        if (var10000 == null) {
            throw new TypeCastException("null cannot be cast to non-null type android.graphics.drawable.GradientDrawable");
        } else {
            GradientDrawable drawable = (GradientDrawable)var10000;
            drawable.setCornerRadius(radius);
            drawable.setColor(defaultBackGroundColor);
            drawable.setStroke(stokeWidth,defaultStokeColor);
            drawable.setShape(GradientDrawable.RECTANGLE);
            return drawable;
        }
    }


    public  GradientDrawable selectedDrawable() {
        Drawable var10000 =getContext().getDrawable(R.drawable.bg_red);
        if (var10000 == null) {
            throw new TypeCastException("null cannot be cast to non-null type android.graphics.drawable.GradientDrawable");
        } else {
            GradientDrawable drawable = (GradientDrawable)var10000;
            drawable.setCornerRadius(this.radius);
            drawable.setStroke(this.stokeWidth,selectedStokeColor);
            drawable.setColor(this.selectedbackGroundColor);
            drawable.setShape(GradientDrawable.RECTANGLE);
            return drawable;
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.orientation == HORIZONTAL) {
           measyreByHorizontal(widthMeasureSpec, heightMeasureSpec);
        } else {
           measureByVertical(widthMeasureSpec, heightMeasureSpec);
        }

    }

    protected  void measureByVertical(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.getChildCount() != 0) {
            int totalHeight =getPaddingTop() +getPaddingBottom();
            int totalWidth = 0;
            int childCount =getChildCount();
            int i = 0;

            for(int var7 = childCount; i < var7; ++i) {
                View child =getChildAt(i);
               measureChild(child, widthMeasureSpec, heightMeasureSpec);
                Intrinsics.checkExpressionValueIsNotNull(child, "child");
                totalWidth = Math.max(totalWidth, child.getMeasuredWidth());
                totalHeight += child.getMeasuredHeight() +itemInternal_vertical;
            }

           setMeasuredDimension(totalWidth +getPaddingLeft() +getPaddingRight(), totalHeight);
        }
    }

    protected  void measyreByHorizontal(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.getChildCount() != 0) {
            int totalHeight =getPaddingTop();
            int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
            int width =getPaddingLeft();
            int childCount =getChildCount();
            int i = 0;

            for(int var8 = childCount; i < var8; ++i) {
                View child =getChildAt(i);
               measureChild(child, widthMeasureSpec, heightMeasureSpec);
                if (totalHeight ==getPaddingTop()) {
                    Intrinsics.checkExpressionValueIsNotNull(child, "child");
                    totalHeight = child.getMeasuredHeight() +getPaddingTop();
                }

                Intrinsics.checkExpressionValueIsNotNull(child, "child");
                if (width + child.getMeasuredWidth() +getPaddingRight() > totalWidth -getPaddingLeft()) {
                    totalHeight += child.getMeasuredHeight() +itemInternal_vertical;
                    width =getPaddingLeft() + child.getMeasuredWidth() +itemInternal_horizontal;
                } else {
                    width += child.getMeasuredWidth() +itemInternal_horizontal;
                }
            }

           setMeasuredDimension(totalWidth, totalHeight +getPaddingBottom());
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if (this.layoutManager != null) {
            layoutManager.onLayout(changed, l, t, r, b);
        }

    }
}


下面是布局管理器的代码

public class LayoutManage {

    @NotNull
    private  Flowlayout flowLayout;


    @NotNull
    public  Flowlayout getFlowLayout() {
        return this.flowLayout;
    }

    public  void onLayout(boolean changed, int l, int t, int r, int b) {
        if (this.flowLayout.orientation == Flowlayout.VERTICAL) {
            this.layoutVertical();
        }

        if (this.flowLayout.orientation == Flowlayout.HORIZONTAL) {
            this.layoutHrizontal();
        }

    }

    private  void layoutHrizontal() {
        int top = this.flowLayout.getPaddingTop();
        int left = 0;
        int childCount = this.flowLayout.getChildCount();
        int i = 0;

        for(int var5 = childCount; i < var5; ++i) {
            View child = this.flowLayout.getChildAt(i);
            Intrinsics.checkExpressionValueIsNotNull(child, "child");
            if (left + child.getMeasuredWidth() > this.flowLayout.getMeasuredWidth() - this.flowLayout.getPaddingLeft()) {
                left = this.flowLayout.getPaddingLeft();
                top += child.getMeasuredHeight() + this.flowLayout.itemInternal_vertical;
            }

            if (i == 0) {
                left = this.flowLayout.getPaddingLeft();
            }

            child.layout(left, top, child.getMeasuredWidth() + left, child.getMeasuredHeight() + top);
            left += child.getMeasuredWidth() + this.flowLayout.itemInternal_horizontal;
        }

    }

    private  void layoutVertical() {
        int top = this.flowLayout.getPaddingTop();
        int childCount = this.flowLayout.getChildCount();
        int i = 0;

        for(int var4 = childCount; i < var4; ++i) {
            View child = this.flowLayout.getChildAt(i);
            int var10001 = this.flowLayout.getPaddingLeft();
            Intrinsics.checkExpressionValueIsNotNull(child, "child");
            child.layout(var10001, top, child.getMeasuredWidth() + this.flowLayout.getPaddingLeft(), child.getMeasuredHeight() + top);
            top += child.getMeasuredHeight() + this.flowLayout.itemInternal_vertical;
        }

    }

    public LayoutManage(@NotNull Flowlayout flowLayout) {

        this.flowLayout = flowLayout;
    }
}



由于代码太简单，我就不作太多的解释了。我相信，这个流式控件，几乎能满足绝大多数的需求了吧
--------------------- 
作者：夜听海雨 
