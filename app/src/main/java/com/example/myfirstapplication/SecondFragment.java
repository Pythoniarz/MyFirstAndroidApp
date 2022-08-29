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

public class SecondFragment extends Fragment {

//    private FragmentSecondBinding binding;
    private BinauralSoundPlayer binauralSoundPlayer;
    EditText leftFreq;
    EditText rightFreq;
    double freqChange;

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
        freqChange = 0.5;
        return fragmentSecondLayout;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textView = view.getRootView().findViewById(R.id.bitrate);
        textView.setText(Integer.toString(binauralSoundPlayer.samplingRate));
        textView = view.getRootView().findViewById(R.id.freq_change);
        textView.setText(Double.toString(freqChange));

        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        view.findViewById(R.id.button_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binauralSoundPlayer.onPlayClicked(view);
                TextView textView = view.getRootView().findViewById(R.id.textview_random);
                leftFreq.setText(""+binauralSoundPlayer.leftFreq);
                rightFreq.setText(""+binauralSoundPlayer.rightFreq);
                countDiff();
                specifyWave();
            }
        });

        view.findViewById(R.id.button_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binauralSoundPlayer.onStopClicked(view);
                TextView textView = view.getRootView().findViewById(R.id.textview_random);
                double diff = (binauralSoundPlayer.leftFreq - binauralSoundPlayer.rightFreq);
                textView.setText("| |");
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

        view.findViewById(R.id.left_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binauralSoundPlayer.leftFreq += freqChange;
                leftFreq.setText(""+binauralSoundPlayer.leftFreq);
                checkEquality();
                countDiff();
                specifyWave();
            }
        });

        view.findViewById(R.id.left_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binauralSoundPlayer.leftFreq -= freqChange;
                leftFreq.setText(""+binauralSoundPlayer.leftFreq);
                checkEquality();
                countDiff();
                specifyWave();
            }
        });

        view.findViewById(R.id.right_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binauralSoundPlayer.rightFreq += freqChange;
                rightFreq.setText(""+binauralSoundPlayer.rightFreq);
                checkEquality();
                countDiff();
                specifyWave();
            }

        });view.findViewById(R.id.right_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binauralSoundPlayer.rightFreq -= freqChange;
                rightFreq.setText(""+binauralSoundPlayer.rightFreq);
                checkEquality();
                countDiff();
                specifyWave();
            }
        });

        view.findViewById(R.id.bitrate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = view.getRootView().findViewById(R.id.bitrate);
                binauralSoundPlayer.setBitrate(Integer.parseInt(textView.getText().toString()));
            }
        });

        view.findViewById(R.id.freq_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = view.getRootView().findViewById(R.id.freq_change);
                freqChange = Double.parseDouble(textView.getText().toString());
            }
        });

        /*
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
         */
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null;
    }

    private void checkEquality() {
        TextView diff = getView().findViewById(R.id.diff);
        if (binauralSoundPlayer.leftFreq > binauralSoundPlayer.rightFreq) {
            diff.setText(">");
        } else if (binauralSoundPlayer.leftFreq < binauralSoundPlayer.rightFreq) {
            diff.setText("<");
        } else {
            diff.setText("=");
        }
    }

    private void countDiff() {
        TextView diff = getView().findViewById(R.id.textview_random);
        diff.setText(Math.abs(binauralSoundPlayer.leftFreq - binauralSoundPlayer.rightFreq) + "Hz");
    }

    private void specifyWave() {
        double freqDiff = Math.abs(binauralSoundPlayer.leftFreq - binauralSoundPlayer.rightFreq);
        TextView wave = getView().findViewById(R.id.wave);
        if (freqDiff == 0) {
            wave.setText("too low");
        } else if(freqDiff <= 3) {
            wave.setText("DELTA");
        } else if(freqDiff <= 8) {
            wave.setText("THETA");
        } else if(freqDiff <= 12) {
            wave.setText("ALPHA");
        } else if(freqDiff <= 38) {
            wave.setText("BETA");
        } else if(freqDiff <= 48) {
            wave.setText("GAMMA");
        } else {
            wave.setText("too high");
        }
    }

//    private double getFreqChange() {
//        TypedValue typedValue = new TypedValue();
//        getResources().getValue(R.dimen.freq_change, typedValue, true);
//        return typedValue.getFloat();
//    }
}