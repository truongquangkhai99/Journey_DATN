package com.example.journey_datn.Model.bsimagepicker;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.journey_datn.R;

import java.util.ArrayList;
import java.util.List;

public class ImageTileAdapter  extends RecyclerView.Adapter<ImageTileAdapter.BaseViewHolder>{
    private static final int VIEWTYPE_CAMERA = 101;
    private static final int VIEWTYPE_IMAGE = 103;
    private static final int VIEWTYPE_DUMMY = 104;
    private static final int VIEWTYPE_BOTTOM_SPACE = 105;

    protected Context context;
    protected List<Uri> imageList;
    protected boolean isMultiSelect;
    protected List<Uri> selectedFiles;
    protected int maximumSelectionCount = Integer.MAX_VALUE;
    protected int nonListItemCount;
    private boolean showCameraTile;

    private View.OnClickListener cameraTileOnClickListener;
    private View.OnClickListener galleryTileOnClickListener;
    private View.OnClickListener imageTileOnClickListener;

    public interface OnSelectedCountChangeListener {
        void onSelectedCountChange(int currentCount);
    }

    private OnSelectedCountChangeListener onSelectedCountChangeListener;

    public interface OnOverSelectListener {
        void onOverSelect();
    }

    private OnOverSelectListener onOverSelectListener;
    private BSImagePicker.ImageLoaderDelegate imageLoaderDelegate;

    public ImageTileAdapter(
            Context context,
            BSImagePicker.ImageLoaderDelegate imageLoaderDelegate,
            boolean isMultiSelect,
            boolean showCameraTile) {
        super();
        this.context = context;
        this.isMultiSelect = isMultiSelect;
        selectedFiles = new ArrayList<>();
        this.showCameraTile = showCameraTile;
        this.imageLoaderDelegate = imageLoaderDelegate;
        if (isMultiSelect) {
            nonListItemCount = 0;
        }
    }

    @Override
    public ImageTileAdapter.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEWTYPE_CAMERA:
                return new CameraTileViewHolder(LayoutInflater.from(context).inflate(R.layout.item_picker_camera_tile, parent, false));
            case VIEWTYPE_DUMMY:
                return new DummyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_picker_dummy_tile, parent, false));
            case VIEWTYPE_BOTTOM_SPACE:
                View view = new View(context);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dp2px(48));
                view.setLayoutParams(lp);
                return new DummyViewHolder(view);
            default:
                return new ImageTileViewHolder(LayoutInflater.from(context).inflate(R.layout.item_picker_image_tile, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ImageTileAdapter.BaseViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (!isMultiSelect) {
            return imageList == null ? 16 : nonListItemCount + imageList.size();
        } else {
            return imageList == null ? 16 : imageList.size() + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (!isMultiSelect) {
            switch (position) {
                case 0:
                    if (showCameraTile) {
                        return VIEWTYPE_CAMERA;
                    }
                    else {
                        return imageList == null ? VIEWTYPE_DUMMY : VIEWTYPE_IMAGE;
                    }
                default:
                    return imageList == null ? VIEWTYPE_DUMMY : VIEWTYPE_IMAGE;
            }
        } else {
            if (position == getItemCount() - 1) return VIEWTYPE_BOTTOM_SPACE;
            return imageList == null ? VIEWTYPE_DUMMY : VIEWTYPE_IMAGE;
        }
    }

    public void setSelectedFiles(List<Uri> selectedFiles) {
        this.selectedFiles = selectedFiles;
        notifyDataSetChanged();
        if (onSelectedCountChangeListener != null)
            onSelectedCountChangeListener.onSelectedCountChange(selectedFiles.size());
    }

    public void setImageList(List<Uri> imageList) {
        this.imageList = imageList;
        notifyDataSetChanged();
    }

    public void setCameraTileOnClickListener(View.OnClickListener cameraTileOnClickListener) {
        this.cameraTileOnClickListener = cameraTileOnClickListener;
    }

    public void setGalleryTileOnClickListener(View.OnClickListener galleryTileOnClickListener) {
        this.galleryTileOnClickListener = galleryTileOnClickListener;
    }

    public void setImageTileOnClickListener(View.OnClickListener imageTileOnClickListener) {
        this.imageTileOnClickListener = imageTileOnClickListener;
    }

    public void setOnSelectedCountChangeListener(OnSelectedCountChangeListener onSelectedCountChangeListener) {
        this.onSelectedCountChangeListener = onSelectedCountChangeListener;
    }

    public void setMaximumSelectionCount(int maximumSelectionCount) {
        this.maximumSelectionCount = maximumSelectionCount;
    }

    public void setOnOverSelectListener(OnOverSelectListener onOverSelectListener) {
        this.onOverSelectListener = onOverSelectListener;
    }

    public abstract static class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bind(int position);

    }

    public class CameraTileViewHolder extends BaseViewHolder {

        public CameraTileViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(cameraTileOnClickListener);
        }

        @Override
        public void bind(int position) {

        }
    }

    public class GalleryTileViewHolder extends BaseViewHolder {

        public GalleryTileViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(galleryTileOnClickListener);
        }

        @Override
        public void bind(int position) {

        }
    }

    public class ImageTileViewHolder extends BaseViewHolder {

        View darken;
        ImageView ivImage, ivTick;

        public ImageTileViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.item_imageTile);
            darken = itemView.findViewById(R.id.imageTile_selected_darken);
            ivTick = itemView.findViewById(R.id.imageTile_selected);
            if (!isMultiSelect) {
                itemView.setOnClickListener(imageTileOnClickListener);
            } else {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri thisFile = imageList.get(getAdapterPosition());
                        if (selectedFiles.contains(thisFile)) {
                            selectedFiles.remove(thisFile);
                            notifyItemChanged(getAdapterPosition());
                        } else {
                            if (selectedFiles.size() == maximumSelectionCount) {
                                if (onOverSelectListener != null)
                                    onOverSelectListener.onOverSelect();
                                return;
                            } else {
                                selectedFiles.add(thisFile);
                                notifyItemChanged(getAdapterPosition());
                            }
                        }
                        if (onSelectedCountChangeListener != null) {
                            onSelectedCountChangeListener.onSelectedCountChange(selectedFiles.size());
                        }
                    }
                });
            }
        }

        public void bind(int position) {
            if (imageList == null) return;
            Uri imageFile = imageList.get(position - nonListItemCount);
            itemView.setTag(imageFile);
            imageLoaderDelegate.loadImage(imageFile, ivImage);
            darken.setVisibility(selectedFiles.contains(imageFile) ? View.VISIBLE : View.INVISIBLE);
            ivTick.setVisibility(selectedFiles.contains(imageFile) ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public class DummyViewHolder extends BaseViewHolder {


        public DummyViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(int position) {

        }
    }
}
