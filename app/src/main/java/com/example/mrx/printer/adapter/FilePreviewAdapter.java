package com.example.mrx.printer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.example.mrx.printer.R;
import com.example.mrx.printer.customView.FileView;
import com.example.mrx.printer.model.FileDirectory;
import com.example.mrx.printer.util.MyApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/18.
 */

public class FilePreviewAdapter extends RecyclerView.Adapter {

    private static final int VIEW_DISK = 0;
    private static final int VIEW_BACK = 1;
    private static final int VIEW_DIR = 2;
    private static final int VIEW_PRINT = 3;
    private static final int VIEW_OTHER = 4;

    private List<FileDirectory> mDirectoryList;

    private FileDirectory currentDir;

    private View selectedView;

    public FilePreviewAdapter(FileDirectory fileDirectory) {
        mDirectoryList = new ArrayList<>();
        if (fileDirectory != null) {
            mDirectoryList.add(fileDirectory);
            currentDir = fileDirectory;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (currentDir.isDiskDir) {
            return VIEW_DISK;
        }

        if (!currentDir.isRoot && position == 0) {
            return VIEW_BACK;
        }

        File file;
        if (currentDir.isRoot) {
            file = currentDir.fileList.get(position);
        } else {
            file = currentDir.fileList.get(position - 1);
        }
        if (FileUtils.isDir(file)) {
            return VIEW_DIR;
        } else if (FileDirectory.isPrintFile(file)) {
            return VIEW_PRINT;
        } else {
            return VIEW_OTHER;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FileView fileView = new FileView(MyApplication.getContext());

        return new ViewHolder(fileView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FileView fileView = (FileView) holder.itemView;
        int viewType = holder.getItemViewType();
        setItemOnClickListener(fileView, viewType, position);
        if (viewType == VIEW_DISK) {
            fileView.getFileIcon().setImageResource(R.drawable.file_disk);
            fileView.getFileName().setText("磁盘" + position);
            return;
        }

        if (viewType == VIEW_BACK) {
            fileView.getFileIcon().setImageResource(R.drawable.file_back);
            return;
        }

        File file;
        if (currentDir.isRoot) {
            file = currentDir.fileList.get(position);
        } else {
            file = currentDir.fileList.get(position - 1);
        }
        fileView.getFileName().setText(file.getName());
        if (viewType == VIEW_DIR) {
            fileView.getFileIcon().setImageResource(R.drawable.directory);
        } else if (viewType == VIEW_PRINT) {
            fileView.getFileIcon().setImageResource(R.drawable.file_print);
        } else {
            fileView.getFileIcon().setImageResource(R.drawable.file_other);
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (currentDir != null) {
            int fileNum = currentDir.fileNum();
            count += fileNum;
            if (!currentDir.isRoot) {
                count += 1;
            }
        }
        return count;
    }

    private void setItemOnClickListener(View item, int viewType, final int position) {
        if (viewType == VIEW_DISK || viewType == VIEW_DIR) {
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File file;
                    if (currentDir.isRoot) {
                        file = currentDir.fileList.get(position);
                    } else {
                        file = currentDir.fileList.get(position - 1);
                    }
                    FileDirectory directory = new FileDirectory(file);
                    mDirectoryList.add(directory);
                    currentDir = directory;
                    notifyDataSetChanged();
                }
            });
        } else if (viewType == VIEW_PRINT || viewType == VIEW_OTHER) {
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedView != null) {
                        selectedView.setSelected(false);
                    }
                    view.setSelected(true);
                    selectedView = view;
                }
            });
        } else if (viewType == VIEW_BACK) {
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDirectoryList.remove(currentDir);
                    currentDir = mDirectoryList.get(mDirectoryList.size() - 1);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
