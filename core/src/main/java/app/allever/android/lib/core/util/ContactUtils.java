package app.allever.android.lib.core.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;

/**
 * Created by Juzhengyuan
 *
 * @Author: Jerry.
 * @Date: 2020/12/17 17
 * @Desc:
 */
public class ContactUtils {

    public static ArrayList<ContactBean> getAllContacts(Context context) {
        ArrayList<ContactBean> contacts = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            //新建一个联系人实例
            ContactBean temp = new ContactBean();
            String contactId = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts._ID));
            //获取联系人姓名
            String name = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            temp.name = name;

            //获取联系人电话号码
            Cursor phoneCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
            while (phoneCursor.moveToNext()) {
                String phone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phone = phone.replace("-", "");
                phone = phone.replace(" ", "");
                temp.mobile = phone;
            }
            //记得要把cursor给close掉
            contacts.add(temp);
            phoneCursor.close();
        }
        cursor.close();
        return contacts;
    }

    public static class ContactBean {
        public String mobile;
        public String name;

        @Override
        public String toString() {
            return "ContactBean{" +
                    "mobile='" + mobile + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
