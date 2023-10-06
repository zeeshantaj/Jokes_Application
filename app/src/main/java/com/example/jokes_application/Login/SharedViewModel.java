package com.example.jokes_application.Login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<String> verificationIdLiveData = new MutableLiveData<>();

    public void setVerificationId(String verificationId) {
        verificationIdLiveData.setValue(verificationId);
    }

    public LiveData<String> getVerificationId() {
        return verificationIdLiveData;
    }
}
