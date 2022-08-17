package com.example.cloneexp.message;
    import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloneexp.R;
//    import com.stfalcon.frescoimageviewer.ImageViewer;

    import java.util.ArrayList;

    public class MessageListAdapter extends RecyclerView.Adapter <MessageListAdapter.RecyclerViewHolder> {
        ArrayList<MessageObject> MessageList;

        public MessageListAdapter(ArrayList<MessageObject> MessageObject) {
            this.MessageList = MessageObject;
        }

        @NonNull
        @Override
        public MessageListAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.message,parent,false);
            ViewGroup.LayoutParams layoutParams= new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(layoutParams);
            MessageListAdapter.RecyclerViewHolder messageVH=new MessageListAdapter.RecyclerViewHolder(view);
            return messageVH;
        }

        @Override
        public void onBindViewHolder(@NonNull final MessageListAdapter.RecyclerViewHolder holder, int position) {
            holder.message.setText(MessageList.get(position).getMessage());
            holder.senderId.setText(MessageList.get(position).getSenderId());
            holder.mediaView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    new ImageViewer.Builder(v.getContext(),MessageList.get(holder.getAdapterPosition()).mediaUrlList)
//                            .setStartPosition(0)
//                            .show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return MessageList.size();
        }

        public class RecyclerViewHolder extends RecyclerView.ViewHolder
        {
            RelativeLayout messageBox;
            TextView message,senderId;
            Button mediaView;

            public RecyclerViewHolder(@NonNull View itemView) {
                super(itemView);
                message=itemView.findViewById(R.id.messageString);
                senderId=itemView.findViewById(R.id.messageSender);
                messageBox=itemView.findViewById(R.id.messageBox);
                mediaView=itemView.findViewById(R.id.mediaView);
            }
        }
    }


