package org.thoughtcrime.securesms;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.thoughtcrime.securesms.recipients.Recipient;
import org.thoughtcrime.securesms.util.ViewUtil;

public class ConversationTitleView extends LinearLayout {

  private static final String TAG = ConversationTitleView.class.getSimpleName();

  private TextView  title;
  private TextView  subtitle;
  private ImageView verified;

  public ConversationTitleView(Context context) {
    this(context, null);
  }

  public ConversationTitleView(Context context, AttributeSet attrs) {
    super(context, attrs);

  }

  @Override
  public void onFinishInflate() {
    super.onFinishInflate();

    this.title    = (TextView)  findViewById(R.id.title);
    this.subtitle = (TextView)  findViewById(R.id.subtitle);
    this.verified = (ImageView) findViewById(R.id.verified_indicator);

    ViewUtil.setTextViewGravityStart(this.title, getContext());
    ViewUtil.setTextViewGravityStart(this.subtitle, getContext());
  }

  public void setTitle(@Nullable Recipient recipient) {
    if      (recipient == null) setComposeTitle();
    else                        setRecipientTitle(recipient);

    if (recipient != null && recipient.isBlocked()) {
      title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_block_white_18dp, 0, 0, 0);
    } else if (recipient != null && recipient.isMuted()) {
      title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_volume_off_white_18dp, 0, 0, 0);
    } else {
      title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }
  }

  public void setVerified(boolean verified) {
    this.verified.setVisibility(verified ? View.VISIBLE : View.GONE);
  }

  private void setComposeTitle() {
    this.title.setText(R.string.ConversationActivity_compose_message);
    this.subtitle.setText(null);
    this.subtitle.setVisibility(View.GONE);
  }

  private void setRecipientTitle(Recipient recipient) {
    if      (recipient.isGroupRecipient())           setGroupRecipientTitle(recipient);
    else if (TextUtils.isEmpty(recipient.getName())) setNonContactRecipientTitle(recipient);
    else                                             setContactRecipientTitle(recipient);
  }

  private void setGroupRecipientTitle(Recipient recipient) {
    this.title.setText(recipient.getName());
    this.subtitle.setText(null);
    this.subtitle.setVisibility(View.GONE);
  }

  private void setNonContactRecipientTitle(Recipient recipient) {
    this.title.setText(recipient.getAddress().serialize());

    if (TextUtils.isEmpty(recipient.getProfileName())) {
      this.subtitle.setText(null);
      this.subtitle.setVisibility(View.GONE);
    } else {
      this.subtitle.setText("~" + recipient.getProfileName());
      this.subtitle.setVisibility(View.VISIBLE);
    }
  }

  private void setContactRecipientTitle(Recipient recipient) {
    this.title.setText(recipient.getName());

    if (recipient.getCustomLabel() != null) this.subtitle.setText(recipient.getCustomLabel());
    else                                    this.subtitle.setText(recipient.getAddress().serialize());

    this.subtitle.setVisibility(View.VISIBLE);
  }
}
