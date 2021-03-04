package Main;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author arifn
 */
public class Substring {

    public static void main(String[] args) {
        String parent = "" +
                "public class RecipientAdapter extends BaseAdapter implements Filterable {\n" +
                "    private final Context context;\n" +
                "    private List<Recipient> recipients;\n" +
                "    private String highlight;\n" +
                "\n" +
                "\n" +
                "    public RecipientAdapter(Context context) {\n" +
                "        super();\n" +
                "        this.context = context;\n" +
                "    }\n" +
                "\n" +
                "    public void setRecipients(List<Recipient> recipients) {\n" +
                "        this.recipients = recipients;\n" +
                "        notifyDataSetChanged();\n" +
                "    }\n" +
                "\n" +
                "    public void setHighlight(String highlight) {\n" +
                "        this.highlight = highlight;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public int getCount() {\n" +
                "        return recipients == null ? 0 : recipients.size();\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public Recipient getItem(int position) {\n" +
                "        return recipients == null ? null : recipients.get(position);\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public long getItemId(int position) {\n" +
                "        return position;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public View getView(int position, View view, ViewGroup parent) {\n" +
                "        if (view == null) {\n" +
                "            view = newView(parent);\n" +
                "        }\n" +
                "\n" +
                "        Recipient recipient = getItem(position);\n" +
                "        bindView(view, recipient);\n" +
                "\n" +
                "        return view;\n" +
                "    }\n" +
                "\n" +
                "    public View newView(ViewGroup parent) {\n" +
                "        View view = LayoutInflater.from(context).inflate(R.layout.recipient_dropdown_item, parent, false);\n" +
                "\n" +
                "        RecipientTokenHolder holder = new RecipientTokenHolder(view);\n" +
                "        view.setTag(holder);\n" +
                "\n" +
                "        return view;\n" +
                "    }\n" +
                "\n" +
                "    public void bindView(View view, Recipient recipient) {\n" +
                "        RecipientTokenHolder holder = (RecipientTokenHolder) view.getTag();\n" +
                "\n" +
                "        holder.name.setText(highlightText(recipient.getDisplayNameOrUnknown(context)));\n" +
                "\n" +
                "        String address = recipient.address.getAddress();\n" +
                "        holder.email.setText(highlightText(address));\n" +
                "\n" +
                "        setContactPhotoOrPlaceholder(context, holder.photo, recipient);\n" +
                "\n" +
                "        Integer cryptoStatusRes = null, cryptoStatusColor = null;\n" +
                "        RecipientCryptoStatus cryptoStatus = recipient.getCryptoStatus();\n" +
                "        switch (cryptoStatus) {\n" +
                "            case AVAILABLE_TRUSTED: {\n" +
                "                cryptoStatusRes = R.drawable.status_lock_dots_3;\n" +
                "                cryptoStatusColor = ThemeUtils.getStyledColor(context, R.attr.openpgp_green);\n" +
                "                break;\n" +
                "            }\n" +
                "            case AVAILABLE_UNTRUSTED: {\n" +
                "                cryptoStatusRes = R.drawable.status_lock_dots_2;\n" +
                "                cryptoStatusColor = ThemeUtils.getStyledColor(context, R.attr.openpgp_orange);\n" +
                "                break;\n" +
                "            }\n" +
                "            case UNAVAILABLE: {\n" +
                "                cryptoStatusRes = R.drawable.status_lock_disabled_dots_1;\n" +
                "                cryptoStatusColor = ThemeUtils.getStyledColor(context, R.attr.openpgp_red);\n" +
                "                break;\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        if (cryptoStatusRes != null) {\n" +
                "            Drawable drawable = ContextCompat.getDrawable(context, cryptoStatusRes);\n" +
                "            DrawableCompat.wrap(drawable);\n" +
                "            DrawableCompat.setTint(drawable.mutate(), cryptoStatusColor);\n" +
                "            holder.cryptoStatusIcon.setImageDrawable(drawable);\n" +
                "            holder.cryptoStatus.setVisibility(View.VISIBLE);\n" +
                "        } else {\n" +
                "            holder.cryptoStatus.setVisibility(View.GONE);\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    public static void setContactPhotoOrPlaceholder(Context context, ImageView imageView, Recipient recipient) {\n" +
                "        ContactPicture.getContactPictureLoader(context).loadContactPicture(recipient, imageView);\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public Filter getFilter() {\n" +
                "        return new Filter() {\n" +
                "            @Override\n" +
                "            protected FilterResults performFiltering(CharSequence constraint) {\n" +
                "                if (recipients == null) {\n" +
                "                    return null;\n" +
                "                }\n" +
                "\n" +
                "                FilterResults result = new FilterResults();\n" +
                "                result.values = recipients;\n" +
                "                result.count = recipients.size();\n" +
                "\n" +
                "                return result;\n" +
                "            }\n" +
                "\n" +
                "            @Override\n" +
                "            protected void publishResults(CharSequence constraint, FilterResults results) {\n" +
                "                notifyDataSetChanged();\n" +
                "            }\n" +
                "        };\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    private static class RecipientTokenHolder {\n" +
                "        public final TextView name;\n" +
                "        public final TextView email;\n" +
                "        public final ImageView photo;\n" +
                "        public final View cryptoStatus;\n" +
                "        public final ImageView cryptoStatusIcon;\n" +
                "\n" +
                "\n" +
                "        public RecipientTokenHolder(View view) {\n" +
                "            name = (TextView) view.findViewById(R.id.text1);\n" +
                "            email = (TextView) view.findViewById(R.id.text2);\n" +
                "            photo = (ImageView) view.findViewById(R.id.contact_photo);\n" +
                "            cryptoStatus = view.findViewById(R.id.contact_crypto_status);\n" +
                "            cryptoStatusIcon = (ImageView) view.findViewById(R.id.contact_crypto_status_icon);\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    public Spannable highlightText(String text) {\n" +
                "        Spannable highlightedSpannable = Spannable.Factory.getInstance().newSpannable(text);\n" +
                "\n" +
                "        if (highlight == null) {\n" +
                "            return highlightedSpannable;\n" +
                "        }\n" +
                "\n" +
                "        Pattern pattern = Pattern.compile(highlight, Pattern.LITERAL | Pattern.CASE_INSENSITIVE);\n" +
                "        Matcher matcher = pattern.matcher(text);\n" +
                "        while (matcher.find()) {\n" +
                "            highlightedSpannable.setSpan(\n" +
                "                    new ForegroundColorSpan(context.getResources().getColor(android.R.color.holo_blue_light)),\n" +
                "                    matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);\n" +
                "        }\n" +
                "\n" +
                "        return highlightedSpannable;\n" +
                "    }\n" +
                "\n" +
                "}";

        String child = "private static class RecipientTokenHolder {\n" +
                "        public final TextView name;\n" +
                "        public final TextView email;\n" +
                "        public final ImageView photo;\n" +
                "        public final View cryptoStatus;\n" +
                "        public final ImageView cryptoStatusIcon;\n" +
                "\n" +
                "\n" +
                "        public RecipientTokenHolder(View view) {\n" +
                "            name = (TextView) view.findViewById(R.id.text1);\n" +
                "            email = (TextView) view.findViewById(R.id.text2);\n" +
                "            photo = (ImageView) view.findViewById(R.id.contact_photo);\n" +
                "            cryptoStatus = view.findViewById(R.id.contact_crypto_status);\n" +
                "            cryptoStatusIcon = (ImageView) view.findViewById(R.id.contact_crypto_status_icon);\n" +
                "        }\n" +
                "    }";

        String newParent = parent.replace(child, "");
        System.out.println(newParent);
    }
}
