/*
 * Copyright 2009-2020 Brian Pellin.
 *     
 * This file is part of KeePassDroid.
 *
 *  KeePassDroid is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  KeePassDroid is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with KeePassDroid.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.keepassdroid;

import android.app.Dialog;
import android.content.Context;

import androidx.appcompat.app.AppCompatDialog;

public class CancelDialog extends AppCompatDialog {

	private boolean mCanceled = false; 
	
	public CancelDialog(Context context) {
		super(context);
	}
	
	public boolean canceled() {
		return mCanceled;
	}

	@Override
	public void cancel() {
		super.cancel();
		mCanceled = true;
	}
}
