package com.micro4blog.oauth;

import android.os.Bundle;

public interface Micro4blogDialogListener {
	
	public void onComplete(Bundle values);

	public void onError(DialogError error);

	public void onCancel();
	
	public void onMicro4blogException(Micro4blogException e);
}
