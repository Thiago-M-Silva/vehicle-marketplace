type Props = {
 
}
export const Footer = ({}: Props) => {
    return ( 
        <footer className="grid grid-cols-4 h-64 mt-10 bg-gray-100 ">
            <div className="bg-red-400 h-full">RED</div>
            <div className="bg-blue-400 h-full">BLUE</div>
            <div className="bg-green-400 h-full">GREEN</div>
            <div className="bg-yellow-400 h-full">YELLOW</div>
        </footer>
    );
}