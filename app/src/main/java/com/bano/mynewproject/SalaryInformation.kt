package com.bano.mynewproject

import android.os.Parcel
import android.os.Parcelable

data class SalaryInformation(val amount: Int, val hoursInDay: Int, val days: Int): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(amount)
        parcel.writeInt(hoursInDay)
        parcel.writeInt(days)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SalaryInformation> {
        override fun createFromParcel(parcel: Parcel): SalaryInformation {
            return SalaryInformation(parcel)
        }

        override fun newArray(size: Int): Array<SalaryInformation?> {
            return arrayOfNulls(size)
        }
    }


}
