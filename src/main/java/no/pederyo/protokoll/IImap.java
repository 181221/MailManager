package no.pederyo.protokoll;

import javax.mail.Folder;

public interface IImap {

    Folder getInbox();

    Folder getFolder(String type);

    Folder[] getAllFolders();

    Folder opprettFolder(String navn);

}
