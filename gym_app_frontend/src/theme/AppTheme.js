import {createTheme} from '@mui/material/styles';
import { blue, green, grey } from "@mui/material/colors";

const myTheme = createTheme({
    palette: {
        primary: {
            main: green[600]
        },
        secondary: {
            main: green[900]
        },
        background: {
            default: grey[50]
        },
        text: {
            primary: "#000000"
        }
    },
});

export default myTheme;