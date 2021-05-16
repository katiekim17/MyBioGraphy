package com.example.mybiography.Recycler;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

enum ButtonsState {
    GONE,
    RIGHT_VISIBLE
}

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private ItemTouchHelperListener listener;
    private boolean swipeBack = false;
    private ButtonsState buttonsShowedState = ButtonsState.GONE;
    private static final float buttonWidth = 200;
    private RectF buttonInstance = null;
    private RecyclerView.ViewHolder currenrtItemViewHolder = null;

    public ItemTouchHelperCallback(ItemTouchHelperListener listener) {
        this.listener = listener;
    }


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
//        int drag_flags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipe_flags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(0, swipe_flags);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return listener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());

    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onItemSwipe(viewHolder.getAdapterPosition());
    }

    //아이템을 터치하거나 스와이프하거나 뷰에 변화가 생길경우 불러오는 함수
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        //아이템이 스와이프 됐을경우 버튼을 그려주기 위해서 스와이프가 됐는지 확인
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            Log.d("onChildDraw", "onChildDraw : " + buttonsShowedState);
            if (buttonsShowedState != ButtonsState.GONE) {
                if (buttonsShowedState == ButtonsState.RIGHT_VISIBLE) {
                    dX = Math.min(dX, -buttonWidth); //왼쪽으로 얼마큼 밀것인지.. dY는 Y축으로 얼마나 가게 할것인지
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            } else {
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            if (buttonsShowedState == ButtonsState.GONE) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        }
        currenrtItemViewHolder = viewHolder;
        //버튼을 그려주는 함수
        drawButtons(c, currenrtItemViewHolder);
    }

    private  void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder) {
        float buttonWidthWithOutPadding = buttonWidth;
        float corners = 5;
        View itemView = viewHolder.itemView;
        Paint p = new Paint();
        buttonInstance = null;
        //왼쪽쪽으로 스와이프 했을때 (오른쪽에 버튼이 보여지게 될 경우)
        if (buttonsShowedState == ButtonsState.RIGHT_VISIBLE) {
            RectF rightButton = new RectF(itemView.getRight() - buttonWidthWithOutPadding,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
            p.setColor(Color.RED);
            c.drawRoundRect(rightButton, corners, corners, p);
            drawText("삭제", c, rightButton, p);
            if (buttonsShowedState == ButtonsState.RIGHT_VISIBLE) {
                buttonInstance = rightButton;
            }
        }
    }

    //버튼의 텍스트 그려주기
    private void drawText(String text, Canvas c, RectF button, Paint p) {
        float textSize = 30;
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        p.setTextSize(textSize);
        float textWidth = p.measureText(text);
        c.drawText(text, button.centerX() - (textWidth / 2), button.centerY() + (textSize / 2), p);
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = false;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);

    }

    //We need some condition to set properly our state — in our touch listener we check how much to the left (or right) user swiped the item. If enough, we change state to show buttons:
    private void setTouchListener(final Canvas c, final RecyclerView recyclerView,
                                  final RecyclerView.ViewHolder viewHolder, final float dX, final float dY,
                                  final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL;
                if (swipeBack) {
                    if (dX < -buttonWidth) buttonsShowedState = ButtonsState.RIGHT_VISIBLE;
                    if (buttonsShowedState != ButtonsState.GONE) {
//                        setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        setItemsClickable(recyclerView, false);
                    }
                }
                return false;
            }
        });
    }

//    private void setTouchDownListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
//        recyclerView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//                }
//                return false;
//            }
//        });
//    }

//    private void setTouchUpListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
//        recyclerView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                ItemTouchHelperCallback.super.onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);
//                recyclerView.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        return false;
//                    }
//                });
//                setItemsClickable(recyclerView, true);
//                swipeBack = false;
//                if (listener != null && buttonInstance != null && buttonInstance.contains(event.getX(), event.getY())) {
//                    if (buttonsShowedState == ButtonsState.RIGHT_VISIBLE) {
//                        listener.onRightClick(viewHolder.getAdapterPosition(), viewHolder);
//                    }
//                }
//                buttonsShowedState = ButtonsState.GONE;
//                currenrtItemViewHolder = null;
//                return false;
//            }
//        });
//    }

    private void setItemsClickable(RecyclerView recyclerView, boolean isClickable) {
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    }

    public void onDraw(Canvas c) {
        if (currenrtItemViewHolder != null) {
            drawButtons(c, currenrtItemViewHolder);
        }
    }
}
