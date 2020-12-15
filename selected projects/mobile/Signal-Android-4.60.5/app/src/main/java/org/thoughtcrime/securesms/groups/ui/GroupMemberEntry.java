package org.thoughtcrime.securesms.groups.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import org.signal.zkgroup.groups.UuidCiphertext;
import org.thoughtcrime.securesms.recipients.Recipient;
import org.thoughtcrime.securesms.util.DefaultValueLiveData;

import java.util.Collection;

public abstract class GroupMemberEntry {

  private final DefaultValueLiveData<Boolean> busy = new DefaultValueLiveData<>(false);

  private GroupMemberEntry() {
  }

  public LiveData<Boolean> getBusy() {
    return busy;
  }

  public void setBusy(boolean busy) {
    this.busy.postValue(busy);
  }

  @Override
  public abstract boolean equals(@Nullable Object obj);

  @Override
  public abstract int hashCode();

  abstract boolean sameId(@NonNull GroupMemberEntry newItem);

  public final static class NewGroupCandidate extends GroupMemberEntry {

    private final DefaultValueLiveData<Boolean> isSelected = new DefaultValueLiveData<>(false);
    private final Recipient                     member;

    public NewGroupCandidate(@NonNull Recipient member) {
      this.member = member;
    }

    public @NonNull Recipient getMember() {
      return member;
    }

    public @NonNull LiveData<Boolean> isSelected() {
      return isSelected;
    }

    public void setSelected(boolean isSelected) {
      this.isSelected.postValue(isSelected);
    }

    @Override
    boolean sameId(@NonNull GroupMemberEntry newItem) {
      if (getClass() != newItem.getClass()) return false;

      return member.getId().equals(((NewGroupCandidate) newItem).member.getId());
    }

    @Override
    public boolean equals(@Nullable Object obj) {
      if (!(obj instanceof NewGroupCandidate)) return false;

      NewGroupCandidate other = (NewGroupCandidate) obj;
      return other.member.equals(member);
    }

    @Override
    public int hashCode() {
      return member.hashCode();
    }
  }

  public final static class FullMember extends GroupMemberEntry {

    private final Recipient member;
    private final boolean   isAdmin;

    public FullMember(@NonNull Recipient member, boolean isAdmin) {
      this.member  = member;
      this.isAdmin = isAdmin;
    }

    public Recipient getMember() {
      return member;
    }

    public boolean isAdmin() {
      return isAdmin;
    }

    @Override
    boolean sameId(@NonNull GroupMemberEntry newItem) {
      if (getClass() != newItem.getClass()) return false;

      return member.getId().equals(((GroupMemberEntry.FullMember) newItem).member.getId());
    }

    @Override
    public boolean equals(@Nullable Object obj) {
      if (!(obj instanceof FullMember)) return false;

      FullMember other = (FullMember) obj;
      return other.member.equals(member) &&
             other.isAdmin == isAdmin;
    }

    @Override
    public int hashCode() {
      return member.hashCode() * 31 + (isAdmin ? 1 : 0);
    }
  }

  public final static class PendingMember extends GroupMemberEntry {
    private final Recipient      invitee;
    private final UuidCiphertext inviteeCipherText;
    private final boolean        cancellable;

    public PendingMember(@NonNull Recipient invitee, @NonNull UuidCiphertext inviteeCipherText, boolean cancellable) {
      this.invitee           = invitee;
      this.inviteeCipherText = inviteeCipherText;
      this.cancellable       = cancellable;
    }

    public Recipient getInvitee() {
      return invitee;
    }

    public UuidCiphertext getInviteeCipherText() {
      return inviteeCipherText;
    }

    public boolean isCancellable() {
      return cancellable;
    }

    @Override
    boolean sameId(@NonNull GroupMemberEntry newItem) {
      if (getClass() != newItem.getClass()) return false;

      return invitee.getId().equals(((GroupMemberEntry.PendingMember) newItem).invitee.getId());
    }

    @Override
    public boolean equals(@Nullable Object obj) {
      if (!(obj instanceof PendingMember)) return false;

      PendingMember other = (PendingMember) obj;
      return other.invitee.equals(invitee) &&
             other.inviteeCipherText.equals(inviteeCipherText) &&
             other.cancellable == cancellable;
    }

    @Override
    public int hashCode() {
      int hash = invitee.hashCode();
      hash *= 31;
      hash += inviteeCipherText.hashCode();
      hash *= 31;
      return hash + (cancellable ? 1 : 0);
    }
  }

  public final static class UnknownPendingMemberCount extends GroupMemberEntry {
    private final Recipient                  inviter;
    private final Collection<UuidCiphertext> ciphertexts;
    private final boolean                    cancellable;

    public UnknownPendingMemberCount(@NonNull Recipient inviter,
                                     @NonNull Collection<UuidCiphertext> ciphertexts,
                                     boolean cancellable) {
      this.inviter     = inviter;
      this.ciphertexts = ciphertexts;
      this.cancellable = cancellable;
    }

    public Recipient getInviter() {
      return inviter;
    }

    public int getInviteCount() {
      return ciphertexts.size();
    }

    public Collection<UuidCiphertext> getCiphertexts() {
      return ciphertexts;
    }

    public boolean isCancellable() {
      return cancellable;
    }

    @Override
    boolean sameId(@NonNull GroupMemberEntry newItem) {
      if (getClass() != newItem.getClass()) return false;

      return inviter.getId().equals(((GroupMemberEntry.UnknownPendingMemberCount) newItem).inviter.getId());
    }

    @Override
    public boolean equals(@Nullable Object obj) {
      if (!(obj instanceof UnknownPendingMemberCount)) return false;

      UnknownPendingMemberCount other = (UnknownPendingMemberCount) obj;
      return other.inviter.equals(inviter) &&
             other.ciphertexts.equals(ciphertexts) &&
             other.cancellable == cancellable;
    }

    @Override
    public int hashCode() {
      int hash = inviter.hashCode();
      hash *= 31;
      hash += ciphertexts.hashCode();
      hash *= 31;
      return hash + (cancellable ? 1 : 0);
    }
  }
}
