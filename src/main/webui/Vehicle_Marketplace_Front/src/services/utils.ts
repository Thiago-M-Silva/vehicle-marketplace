//TODO use react router
export const redirectMethod = (address: string) => (
    window.location.href = address
)

export const isAuthenticated = () => {
    const token = localStorage.getItem("access_token");
    if (!token) return false;

    const payload = JSON.parse(atob(token.split(".")[1]));
    const now = Date.now() / 1000;

    return payload.exp > now;
};