package com.example.shoestore.infrastructure.constants;

public class MessageCode {

    public static final class Message {
        public Message() {
        }

        public static final String MESSAGE_OTP = "Mã OTP của bạn là : ";

        public static final String MESSAGE_EMAIL = "Mã xác nhận của bạn là: ";

        public static final String MESSAGE_ERROR_PHONENUMBER_OR_EMAIL = "Số điện thoại hoặc email không hợp lệ !";

        public static final String MESSAGE_PASSWORD_MISMATCH = "Mật khẩu không trùng khớp !";
        public static final String MESSAGE_PASSWORD_WRONG = "Mật khẩu sai !";

        public static final String MESSAGE_OTP_MISMATCH = "Mã OTP không trùng khớp !";

    }

    public static final class Email {
        public Email() {
        }

        public static final String EMAIL_FROM = "nguyenleduong03@gmail.com";

        public static final String EMAIL_SUBJECT_CONFIRM = "[SHOES STORE] - MÃ XÁC NHẬN !";
        public static final String EMAIL_SUBJECT_CONFIRM_ADMIN = "[SHOES STORE] - XÁC NHẬN NHÂN VIÊN !";
        public static final String EMAIL_TEXT = "Chào mừng bạn đến với hệ sinh thái giày LEATHER GEN\n ";
        public static final String EMAIL_USERNAME = "Username: ";
        public static final String EMAIL_PASSWORD = "Password: ";


    }

    public static final class Excel {
        public static final String FIELD_NOT_BLANK = "field.notBlank";
        public static final String FIELD_MAX_LENGTH = "field.maxLenth";
        public static final String FIELD_MIN_TO_MAX = "field.minToMax";
    }

    public static final class Commom {

        public static final String MAINTENANCE = "common.error.maintenance";
        public static final String NOT_EXIST = "common.error.notExist";
        public static final String NOT_INVALID_TYPE_STRING = "common.error.invalidTypeString";
        public static final String NOT_INVALID_TYPE_NUMBER = "common.error.invalidTypeNumeric";
        public static final String NOT_NULL = "common.error.notNull";
        public static final String NOT_FOUND = "common.error.notFound";
        public static final String EXISTED = "common.error.existed";
        public static final String DATA_TYPE_INVALID = "common.error.dataTypeInvalid";
        public static final String DATE_INCORRECT_FORMAT = "common.error.dateIncorrectFormat";
        public static final String FROM_DATE_GT_TO_DATE = "common.error.fromDateGreatThanToDate";
        public static final String RESULT_EMPTY = "common.error.resultEmpty";
        public static final String TOKEN_EXPIRED = "common.token.expired";
        public static final String TOKEN_UN_SUPPORT = "common.token.unSupport";
        public static final String TOKEN_MALFORMED = "common.token.malFormed";
        public static final String SIGNATURE_INVALID = "common.signature.invalid";
        public static final String TOKEN_ERROR = "common.token.error";
        public static final String FILE_NOT_EMPTY = "common.file.notEmpty";
        public static final String FILE_MAX_SIZE = "common.file.maxSize";
        public static final String FILE_MAX_LENGTH = "common.file.maxLength";
        public static final String FILE_ALLOWED = "common.file.allowed";

    }

    public static final class Login {
        private Login() {
        }

        public static final String USERNAME_NOT_BLANK = "login.username.notBlank";
        public static final String PASSWORD_NOT_BLANK = "login.password.notBlank";

    }

    public static final class Shoes {
        private Shoes() {
        }

        public static final String ENTITY = "shoes";
        public static final String NAME_NOT_NULL = "shoes.name.notNull";
        public static final String NAME_MAX_LENGTH = "shoes.name.maxLength";
        public static final String NAME_NOT_BLANK = "shoes.name.notBlank";
        public static final String TYPE_ID_NOT_NULL = "shoes.typeId.notNull";
        public static final String BRAND_ID_NOT_NULL = "shoes.brandId.notNull";
        public static final String SKIN_TYPE_ID_NOT_NULL = "shoes.skinTypeId.notNull";
        public static final String SOLE_ID_NOT_NULL = "shoes.soleId.notNull";
        public static final String LINING_ID_NOT_NULL = "shoes.liningId.notNull";
        public static final String ORIGIN_ID_NOT_NULL = "shoes.origin.notNull";
        public static final String TOE_ID_NOT_NULL = "shoes.toe.notNull";
        public static final String CUSHION_ID_NOT_NULL = "shoes.cushion.notNull";
        public static final String DESCRIPTION_MAX_LENGTH = "shoes.description.maxLength";
        public static final String EXIST_DB_OTHER = "shoes.exist.other";

    }

    public static final class ShoesDetail {
        private ShoesDetail() {
        }

        public static final String ENTITY = "shoesDetail";

        public static final String ENTITY_NOT_EXIST = "shoesDetail.notExist";
        public static final String ENTITY_NOT_FOUND = "shoesDetail.notFount";
        public static final String SIZE_ID_NOT_NULL = "shoesDetail.sizeId.notNull";
        public static final String COLOR_ID_NOT_NULL = "shoesDetail.colorId.notNull";
        public static final String QUANTITY_NOT_NULL = "shoesDetail.quantity.notNull";
        public static final String STATUS_NOT_NULL = "shoesDetail.status.notNull";
        public static final String PRICE_NOT_NULL = "shoesDetail.price.notNull";
        public static final String QUANTITY_MIN = "shoesDetail.quantity.min";
        public static final String QUANTITY_MAX = "shoesDetail.quantity.max";
        public static final String PRICE_MIN = "shoesDetail.price.min";
        public static final String PRICE_MAX = "shoesDetail.price.max";
        public static final String STATUS = "shoesDetail.price.status";
        public static final String DELETE_ON_BUSINESS = "shoesDetail.deletes.onBusiness";
        public static final String SHOES_STATUS_OUT_OF_STOCK = "shoesDetail.status.outOfStock";

    }

    public static final class DesignStyle {
        private DesignStyle() {
        }

        public static final String ENTITY = "designStyle";
        public static final String NAME_NOT_NULL = "designStyle.name.notNull";
        public static final String NAME_MAX_LENGTH = "designStyle.name.maxLength";
        public static final String EXIST_DB_OTHER = "designStyle.exist.other";

    }

    public static final class Brand {
        private Brand() {
        }

        public static final String ENTITY = "brand";
        public static final String NAME_NOT_NULL = "brand.name.notNull";
        public static final String NAME_MAX_LENGTH = "brand.name.maxLength";
        public static final String EXIST_DB_OTHER = "brand.exist.other";

    }

    public static final class Size {
        private Size() {
        }

        public static final String ENTITY = "size";
        public static final String NAME_NOT_NULL = "size.name.notNull";

        public static final String NAME_MAX_LENGTH = "size.name.maxLength";
        public static final String EXIST_DB_OTHER = "size.exist.other";

    }

    public static final class Color {

        private Color() {
        }

        public static final String ENTITY = "color";
        public static final String NAME_NOT_NULL = "color.name.notNull";
        public static final String NAME_MAX_LENGTH = "color.name.maxLength";
        public static final String EXIST_DB_OTHER = "color.exist.other";

    }

    public static final class SkinType {
        private SkinType() {
        }

        public static final String ENTITY = "skinType";
        public static final String NAME_NOT_NULL = "skinType.name.notNull";
        public static final String NAME_MAX_LENGTH = "skinType.name.maxLength";
        public static final String EXIST_DB_OTHER = "skinType.exist.other";

    }

    public static final class Sole {
        private Sole() {
        }

        public static final String ENTITY = "sole";
        public static final String NAME_NOT_NULL = "sole.name.notNull";
        public static final String NAME_MAX_LENGTH = "sole.name.maxLength";
        public static final String EXIST_DB_OTHER = "sole.exist.other";

    }

    public static final class Lining {
        private Lining() {
        }

        public static final String ENTITY = "lining";
        public static final String NAME_NOT_NULL = "lining.name.notNull";
        public static final String NAME_MAX_LENGTH = "lining.name.maxLength";
        public static final String EXIST_DB_OTHER = "lining.exist.other";

    }

    public static final class Origin {
        private Origin() {
        }

        public static final String ENTITY = "origin";
        public static final String NAME_NOT_NULL = "origin.name.notNull";
        public static final String NAME_MAX_LENGTH = "origin.name.maxLength";
        public static final String EXIST_DB_OTHER = "origin.exist.other";

    }

    public static final class Toe {
        private Toe() {
        }

        public static final String ENTITY = "toe";
        public static final String NAME_NOT_NULL = "toe.name.notNull";
        public static final String NAME_MAX_LENGTH = "toe.name.maxLength";
        public static final String EXIST_DB_OTHER = "toe.exist.other";

    }

    public static final class Cushion {

        private Cushion() {
        }

        public static final String ENTITY = "cushion";
        public static final String NAME_NOT_NULL = "cushion.name.notNull";
        public static final String NAME_MAX_LENGTH = "cushion.name.maxLength";
        public static final String EXIST_DB_OTHER = "cushion.exist.other";

    }

    public static final class Image {
        private Image() {
        }

        public static final String ENTITY = "image";
        public static final String IMAGE_DUPLICATE = "image.duplicate";
        public static final String ALLOWED_IMAGES_SIZE = "images.allowed.size";
    }

    public static final class ImageShoesDetail {
        private ImageShoesDetail() {
        }

        public static final String ENTITY = "image.shoesDetail";
    }

    public static final class Accounts {
        private Accounts() {
        }

        public static final String ACCOUNT_NOT_FOUND = "memberAccount.notFound";
        public static final String ROLES_NOT_FOUND = "memberAccount.roles.notFound";
        public static final String ACCOUNT_ACCESS_DENIED = "memberAccount.accessDenied";
        public static final String USERNAME_NOT_NULL = "memberAccount.username.notnull";
        public static final String USERNAME_MAX_MIN = "memberAccount.username.maxmin";
        public static final String USERNAME_EXISTS = "memberAccount.username.exists";
        public static final String PASSWORD_NOT_NULL = "memberAccount.password.notnull";
        public static final String PASSWORD_MAX_MIN = "memberAccount.password.maxmin";
        public static final String PASSWORD_UPPERCASE = "memberAccount.password.uppercase";
        public static final String PASSWORD_LOWERCASE = "memberAccount.password.lowercase";
        public static final String PASSWORD_NUMBER = "memberAccount.password.number";
        public static final String PASSWORD_SPECIAL = "memberAccount.password.special";
        public static final String CONFIRM_PASSWORD_NOT_NULL = "memberAccount.confirmPassword.notnull";
        public static final String CHANGE_PASSWORD_SUCCESS = "memberAccount.changePassword.success";
        public static final String FULL_NAME_NOT_NULL = "memberAccount.fullname.notnull";
        public static final String FULLNAME_LENGTH = "memberAccount.fullname.length";
        public static final String EMAIL_NOT_NULL = "memberAccount.email.notnull";
        public static final String EMAIL_INVALID = "memberAccount.email.invalid";
        public static final String EMAIL_LENGTH = "memberAccount.email.length";
        public static final String EMAIL_EXISTS = "memberAccount.email.exists";
        public static final String PHONENUMBER_NOT_NULL = "memberAccount.phoneNumber.notnull";
        public static final String PHONENUMBER_LENGTH = "memberAccount.phoneNumber.length";
        public static final String PHONENUMBER_INVALID = "memberAccount.phoneNumber.invalid";
        public static final String PHONENUMBER_EXISTS = "memberAccount.phoneNumber.exists";

        public static final String UNAUTHOR = "memberAccount.unouthor";

        public static final String ADMIN_NOT_EXIST = "memberAccount.admin.notExits";

    }

    public static final class Cart {
        private Cart() {
        }

        public static final String SHOES_NOTNULL = "cart.shoes.notnull";
        public static final String QUANTITY_MIN = "cart.shoes.quantity.min";
        public static final String QUANTITY_MAX = "cart.shoes.quantity.max";
        public static final String QUANTITY_MAX_15 = "cart.shoes.quantity.max.30";
        public static final String CART_FUll = "cart.shoes.cartfull";

    }

    public static final class Discount {

        private Discount() {

        }


        public static final String ENTITY = "discount";
        public static final String NAME_NOT_NULL = "discount.name.notNull";
        public static final String NAME_NOT_BLANK = "discount.name.notBlank";
        public static final String NAME_MAX_LENGTH = "discount.name.maxLength";
        public static final String SALE_PRICE_NOT_NULL = "discount.salePrice.notNull";
        public static final String SALE_NOT_NULL = "discount.sale.notNull";
        public static final String SALE_PERCENT_NOT_NULL = "discount.salePercent.notNull";
        public static final String MIN_PRICE_NOT_NULL = "discount.minPrice.notNull";
        public static final String MIN_PRICE_PROMO_NOT_NULL = "discount.minPricePromo.notNull";
        public static final String DESCRIPTION_NOT_NULL = "discount.description.notNull";
        public static final String DESCRIPTION_NOT_BLANK = "discount.description.notBlank";
        public static final String DESCRIPTION_MAX_LENGTH = "discount.description.maxLength";
        public static final String START_DATE_NOT_NULL = "discount.startDate.notNull";
        public static final String END_DATE_NOT_NULL = "discount.endDate.notNull";
        public static final String STATUS_NOT_NULL = "discount.status.notNull";
        public static final String DATE_NOT_NULL = "discount.endDate.notNull";
        public static final String STATUS_INCORRECT = "discount.status.incorrect";
        public static final String STATUS_CORRECT = "discount.status.correct";
        public static final String START_DATE_ISAFTER_END_DATE = "discount.sale.startDate.isAfterEndDate";
        public static final String START_DATE_ISEQUAL_OR_BIGGER = "discount.sale.startDate.isEqual";
        public static final String IS_NUMBER = "discount.sale.isNumber";
        public static final String GREATER_THAN_ZERO = "discount.sale.greaterThanZero";
        public static final String LESS_THAN_HUNDERED = "discount";
        public static final String STOP_VOUCHER = "discount.voucher.isRunning";
        public static final String PROMOS_IS_RUNNING = "discount.promo.isRunning";
        public static final String GREATER_THAN_FIFTYPERCENT = "discount.sale.greaterFifty";
        public static final String UNLESS_THAN_FIFTY = "discount.sale.greaterFifty";
        public static final String MINPRICE_IS_SMALLER_MAXPRICE = "discount.minimumPrice";
        public static final String ID_NOT_NULL = "discount.id.notNull";
        public static final String START_DATE_IS_RUNNING = "discountPeriod.startDate.isRunning";
        public static final String MINPRICE_IS_BIGGER_ZERO = "discount.minPrice.biggerZero";
        public static final String END_DATE_IS_EXPIRED = "discount.isExpired";
        public static final String IS_SMALLER_TWELVE = "discount.isSmaller";
        public static final String IS_BIGGER_ZERO = "discount.isBigger";
        public static final String IS_USING = "discount.isUsing";
        public static final String UNLESS_THAN_FIFTY_PERCENT = "discount.sale.greaterFifty";
        public static final String QUANTITY_NOT_NULL = "discount.quantity.notNull";
        public static final String SALE_PRICE_MIN_PRICE = "discount.salePrice.minPrice";
        public static final String IS_BIGGER_MILLION = "discount.salePrice.isBiggerMillion";
        public static final String IS_EQUAL = "discount.salePrice.equals";
        public static final String IS_MILLION = "discount.isMilliion";
        public static final String SALE_IS_BIGGER_THOUSAND = "discount.salePrice.isThousand";

    }

    public static final class DiscountPeriod {

        private DiscountPeriod() {

        }

        public static final String ENTITY = "discountPeriod";

        public static final String NAME_NOT_NULL = "discountPeriod.name.notNull";
        public static final String NAME_NOT_BLANK = "discountPeriod.name.notBlank";
        public static final String NAME_MAX_LENGTH = "discountPeriod.name.maxLength";
        public static final String SALE_NOT_NULL = "discountPeriod.sale.notNull";
        public static final String START_DATE_NOT_NULL = "discountPeriod.startDate.notNull";
        public static final String END_DATE_NOT_NULL = "discountPeriod.endDate.notNull";
        public static final String SALE_PERCENT_NOT_NULL = "discountPeriod.salePercent.notNull";
        public static final String MINPRICE_IS_SMALLER_MAXPRICE = "discountPeriod.minimumPrice";
        public static final String TYPE_PERIOD_NOT_NULL = "discountPeriod.typePeriod.notNull";
        public static final String START_DATE_IS_AFTER_END_DATE = "discount.sale.startDate.isAfterEndDate";
        public static final String START_DATE_IS_EQUAL_OR_BIGGER = "discount.sale.startDate.isEqual";
        public static final String IS_NUMBER = "discountPeriod.isNumber";
        public static final String GREATER_THAN_ZERO = "discountPeriod.greaterZero";
        public static final String SMALLER_THAN_SIXTY_FIVE = "discountPeriod.smallerThan";
        public static final String DISCOUNT_IS_RUNNING = "discountPeriod.isRunning";
        public static final String SALE_MIN_MAX = "discountPeriod.sale.minMax";
        public static final String DISCOUNT_IS_WAITING = "discountPeriod.isWaiting";
        public static final String MINPRICE_NOT_NULL = "discountPeriod.minPrice.notNull";
        public static final String IS_STOP = "discountPeriod.startDate.isStop";

    }

    public static final class Rate {

        private Rate() {

        }

        public static final String ENTITY = "rate";

        public static final String MUST_SIGN_IN = "rate.mustSignIn";
    }

    public static final class Comment {

        private Comment() {

        }

        public static final String ENTITY = "comment";
        public static final String MUST_SIGN_IN = "comment.mustSignIn";
        public static final String MAX_SIZE = "comment.maxSize";
    }


    public static final class Exchange {
        private Exchange() {

        }

        public static final String QUANTITY_EXCHANGE = "exchange.quantity";
        public static final String BILL_RECEIVED = "exchange.bill";
        public static final String SHOES_NOT_EXISTS = "exchange.shoes.notexists";

    }

    public static final class DeliveryOrder {
        private DeliveryOrder() {

        }

        public static final String DELIVERY_ORDER_NULL = "deliveryorder.null";
        public static final String DELIVERY_ADDRESS = "deliveryorder.address.null";
        public static final String DELIVERY_ADDRESS_MAX = "deliveryorder.address.max";
        public static final String DELIVERY_RECIPIENT_NAME = "deliveryorder.name.null";
        public static final String DELIVERY_RECIPIENT_NAME_MAX = "deliveryorder.name.max";
        public static final String DELIVERY_RECIPIENT_PHONE = "deliveryorder.phone.null";
        public static final String DELIVERY_RECIPIENT_PHONE_MAX = "deliveryorder.phone.max";
        public static final String DELIVERY_DELIVERY_COST = "deliveryorder.cost.null";
        public static final String DELIVERY_DELIVERY_COST_MAX = "deliveryorder.cost.max";
        public static final String DELIVERY_DELIVERY_COST_MIN = "deliveryorder.cost.min";


    }

}
