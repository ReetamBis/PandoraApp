import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pandora.R;

import java.util.ArrayList;

public class HomePageRecycleAdapter extends RecyclerView.Adapter<HomePageRecycleAdapter.PlaceHolder>
{
    ArrayList<String> subject=new ArrayList<>();
    public HomePageRecycleAdapter(Context context,ArrayList<String> sub)
    {
        subject=sub;
    }
    public class PlaceHolder extends RecyclerView.ViewHolder
    {
        public PlaceHolder(View itemView)
        {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public PlaceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


}
