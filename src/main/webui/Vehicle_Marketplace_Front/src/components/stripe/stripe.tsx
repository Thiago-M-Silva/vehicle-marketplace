import { Elements } from "@stripe/react-stripe-js";
import { stripePromise } from "../../services/stripe";

export const StripeCheckout = () => {
  return (
    <Elements stripe={stripePromise}>
      {/* <CheckoutForm /> */}
    </Elements>
  );
}


//TODO: see how to manage this
/*
import { useStripe, useElements, CardElement } from "@stripe/react-stripe-js";

const stripe = useStripe();
const elements = useElements();

const handleSubmit = async (e) => {
  e.preventDefault();

  const result = await stripe.confirmCardPayment(clientSecret, {
    payment_method: {
      card: elements.getElement(CardElement),
    },
  });

  if (result.error) {
    console.log(result.error.message);
  } else {
    console.log("Payment successful!");
  }
};
*/