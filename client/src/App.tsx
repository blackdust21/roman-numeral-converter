import React, { useState } from "react";
import {
  Provider,
  defaultTheme,
  Button,
  TextField,
  View,
  Heading,
  Text,
  Flex
} from "@adobe/react-spectrum";
import axios from "axios";

const App = () => {
  const [inputValue, setInputValue] = useState<string>("");
  const [outputValue, setOutputValue] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  const handleConvert = async () => {
    setError(null);
    setOutputValue(null);
    const num = parseInt(inputValue, 10);
    if (isNaN(num) || num < 1 || num > 3999) {
      setError("Please enter a number between 1 and 3999.");
      return;
    }

    try {
      const response = await axios.get(
        `http://localhost:8080/romannumeral?query=${num}`
      );
      setOutputValue(response.data.output);
    } catch (err) {
      setError("Failed to fetch conversion. Please make sure that the backend service is running. Follow instructions to run the Java Springboot backend service.");
    }
  };

  return (
    <Provider theme={defaultTheme}>
      <View padding="size-300">
        <Heading level={1}>Roman Number Converter</Heading>
        <Flex direction="column" gap="size-200" alignItems="start">
          <TextField
            label="Enter a number"
            value={inputValue}
            onChange={(val) => setInputValue(val)}
            inputMode="numeric"
            pattern="[0-9]*"
          />
          <Button variant="cta" onPress={handleConvert}>Convert</Button>
        </Flex>
        {outputValue && <Text marginTop="size-200">Output: {outputValue}</Text>}
        {error && <Text marginTop="size-200" UNSAFE_style={{ color: "red" }}>{error}</Text>}
      </View>
    </Provider>
  );
};

export default App;
