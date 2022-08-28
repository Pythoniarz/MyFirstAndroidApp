package com.example.myfirstapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Random;

public class SecondFragment extends Fragment {

//    private FragmentSecondBinding binding;
    private BinauralSoundPlayer binauralSoundPlayer;
    EditText leftFreq;
    EditText rightFreq;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

//        binding = FragmentSecondBinding.inflate(inflater, container, false);
        View fragmentSecondLayout = inflater.inflate(R.layout.fragment_second, container, false);
        binauralSoundPlayer = new BinauralSoundPlayer();
        leftFreq = fragmentSecondLayout.findViewById(R.id.left_frequency);
        rightFreq = fragmentSecondLayout.findViewById(R.id.right_frequency);
        return fragmentSecondLayout;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        view.findViewById(R.id.play_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binauralSoundPlayer.onPlayClicked(view);
            }
        });

        view.findViewById(R.id.stop_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binauralSoundPlayer.onStopClicked(view);
            }
        });

        view.findViewById(R.id.left_frequency).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String freq = leftFreq.getText().toString();
                if(!freq.isEmpty())
                    try
                    {
                        double value = Double.parseDouble(freq);
                        binauralSoundPlayer.leftFreq = value;
                        // it means it is double
                    } catch (Exception e1) {
                        // this means it is not double
                        e1.printStackTrace();
                    }

            }
        });

        view.findViewById(R.id.right_frequency).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String freq = rightFreq.getText().toString();
                if(!freq.isEmpty())
                    try
                    {
                        double value = Double.parseDouble(freq);
                        binauralSoundPlayer.rightFreq = value;
                        // it means it is double
                    } catch (Exception e1) {
                        // this means it is not double
                        e1.printStackTrace();
                    }

            }
        });

        Integer count = SecondFragmentArgs.fromBundle(getArguments()).getMyArg();
        String countText = getString(R.string.random_heading, count);
        TextView headerView = view.getRootView().findViewById(R.id.textview_header);
        headerView.setText(countText);

        Random random = new java.util.Random();
        Integer randomNumber = 0;
        if (count > 0) {
            randomNumber = random.nextInt(count + 1);
        }

        TextView randomView = view.getRootView().findViewById(R.id.textview_random);
        randomView.setText(randomNumber.toString());

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null;
    }

}